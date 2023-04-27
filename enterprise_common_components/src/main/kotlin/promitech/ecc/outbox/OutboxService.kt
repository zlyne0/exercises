package promitech.ecc.outbox

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import promitech.ecc.TransactionService
import promitech.ecc.async.AsyncService
import promitech.ecc.blob.json.JsonService
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.time.Clock
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.LockModeType

interface OutboxAction<T: Any> {
    fun action(data: T)
}

data class OutboxItemConfiguration<T : Any>(val clazz: Class<T>, val outboxAction: OutboxAction<T>)

class OutboxRegistryService {

    private val itemConfigurations = mutableMapOf<String, OutboxItemConfiguration<*>>()

    fun <T : Any> registry(clazz: Class<T>, outboxAction: OutboxAction<T>) {
        itemConfigurations.put(clazz.name, OutboxItemConfiguration(clazz, outboxAction))
    }

    fun <T: Any> loadConfiguration(type: String): OutboxItemConfiguration<T>? {
        return itemConfigurations.get(type) as OutboxItemConfiguration<T>?
    }

}

@Service
class OutboxService(
    private val entityManager: EntityManager,
    private val jsonService: JsonService,
    private val transactionService: TransactionService,
    private val outboxRegistryService: OutboxRegistryService,
    private val lockTimeout: String,
    private val clock: Clock
) {

    interface AfterCompletionListener {
        fun action(outboxEntityId: Long)
    }
    private val afterCompletionListeners = ArrayList<AfterCompletionListener>()

    private val asyncService = AsyncService()

    /**
     * Require run in transaction because its essential of outbox to save data and send.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    fun <T: Any> send(objectData: T): OutboxEntityId {
        val handlerConfiguration: OutboxItemConfiguration<Any>? = outboxRegistryService.loadConfiguration(objectData::class.java.name)
        if (handlerConfiguration == null) {
            throw IllegalArgumentException("can not find handler configuration for objectType: " + objectData::class.java.name)
        }

        val jsonId = jsonService.saveObject(objectData)
        val outboxEntity = OutboxEntity(jsonObjectId = jsonId, type = objectData::class.java.name)
        entityManager.persist(outboxEntity)

        val outboxEntityId: Long = outboxEntity.id!!

        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                asyncService.async({
                    transactionService.runInNewTransaction {
                        sendSingleMessage(outboxEntityId)
                    }
                })
            }
        })
        return OutboxEntityId(outboxEntityId)
    }

    private fun sendSingleMessage(outboxEntityId: Long) {
        TransactionSynchronizationManager.registerSynchronization(object: TransactionSynchronization {
            override fun afterCompletion(status: Int) {
                afterCompletionListeners.forEach { listener -> listener.action(outboxEntityId) }
            }
        })

        val list = entityManager.createQuery("select oe from OutboxEntity oe where oe.id = :id")
            .setHint("javax.persistence.lock.timeout", lockTimeout)
            .setParameter("id", outboxEntityId)
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .resultList
        if (list.isEmpty()) {
            return
        }
        val outboxEntity: OutboxEntity = list.get(0) as OutboxEntity

        val configuration: OutboxItemConfiguration<Any>? = outboxRegistryService.loadConfiguration(outboxEntity.type)
        if (configuration == null) {
            throw IllegalStateException("can not find action for outboxEntity type: " + outboxEntity.type)
        }
        val jsonObject = jsonService.loadObject(outboxEntity.jsonObjectId, configuration.clazz)
        try {
            configuration.outboxAction.action(jsonObject)

            entityManager.createQuery("delete from OutboxEntity oe where oe.id = :id")
                .setParameter("id", outboxEntityId)
                .executeUpdate()
        } catch (e: Exception) {
            outboxEntity.increaseErrorCount(LocalDateTime.now(clock))
        }
        entityManager.flush()
    }

    @Transactional(readOnly = true)
    fun <T> countByType(clazz: Class<T>): Long {
        val count = entityManager.createQuery("select count(oe) from OutboxEntity oe where oe.type = :type")
            .setParameter("type", clazz.name)
            .singleResult
        return count as Long
    }

    @Transactional(readOnly = true)
    fun load(outboxEntityId: OutboxEntityId): OutboxEntity {
        return entityManager.createQuery("select oe from OutboxEntity oe where oe.id = :id")
            .setParameter("id", outboxEntityId.value)
            .singleResult as OutboxEntity
    }

    fun addAfterCompletionListener(afterCompletionListener: AfterCompletionListener) {
        this.afterCompletionListeners.add(afterCompletionListener)
    }
}