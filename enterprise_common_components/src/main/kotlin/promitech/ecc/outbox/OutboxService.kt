package promitech.ecc.outbox

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import promitech.ecc.TransactionService
import promitech.ecc.async.AsyncService
import promitech.ecc.blob.json.JsonService
import java.time.Clock
import java.time.LocalDateTime

interface OutboxAction<T: Any> {
    fun action(data: T)
}

data class OutboxItemConfiguration<T : Any>(val clazz: Class<T>, val outboxAction: OutboxAction<T>)

class OutboxRegistryService {

    private val itemConfigurations = mutableMapOf<String, OutboxItemConfiguration<*>>()

    fun <T : Any> registry(clazz: Class<T>, outboxAction: OutboxAction<T>) {
        itemConfigurations.put(clazz.name, OutboxItemConfiguration(clazz, outboxAction))
    }

    fun <T: Any> loadConfiguration(objectData: T): OutboxItemConfiguration<T> {
        return loadConfiguration(objectData::class.java.name)
    }

    fun <T: Any> loadConfiguration(type: String): OutboxItemConfiguration<T> {
        val configuration = itemConfigurations.get(type) as OutboxItemConfiguration<T>?
        if (configuration == null) {
            throw IllegalArgumentException("can not find handler configuration for objectType: " + type)
        }
        return configuration
    }
}

@Service
class OutboxService(
    private val repository: OutboxRepository,
    private val jsonService: JsonService,
    private val transactionService: TransactionService,
    private val outboxRegistryService: OutboxRegistryService,
    private val lockTimeout: String,
    private val clock: Clock
) {

    interface AfterCompletionListener {
        fun action(outboxEntityId: OutboxEntityId)
    }
    private val afterCompletionListeners = ArrayList<AfterCompletionListener>()

    private val asyncService = AsyncService()

    /**
     * Require run in transaction because its essential of outbox to save data and send.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    fun <T: Any> send(objectData: T): OutboxEntityId {
        outboxRegistryService.loadConfiguration(objectData)

        val jsonId = jsonService.saveObject(objectData)
        val outboxEntity = repository.create(jsonId, objectData::class.java.name)
        val outboxEntityId = outboxEntity.id

        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                asyncService.async({
                    transactionService.runInNewTransaction {
                        sendSingleMessage(outboxEntityId)
                    }
                })
            }
        })
        return outboxEntityId
    }

    private fun sendSingleMessage(outboxEntityId: OutboxEntityId) {
        TransactionSynchronizationManager.registerSynchronization(object: TransactionSynchronization {
            override fun afterCompletion(status: Int) {
                afterCompletionListeners.forEach { listener -> listener.action(outboxEntityId) }
            }
        })

        val outboxEntity = repository.findAndLock(outboxEntityId, lockTimeout)
        if (outboxEntity == null) {
            return
        }

        val configuration: OutboxItemConfiguration<Any> = outboxRegistryService.loadConfiguration(outboxEntity.type)
        val jsonObject = jsonService.loadObject(outboxEntity.jsonObjectId, configuration.clazz)
        try {
            configuration.outboxAction.action(jsonObject)
            repository.delete(outboxEntityId)
        } catch (e: Exception) {
            outboxEntity.increaseErrorCount(LocalDateTime.now(clock))
        }
    }

    @Transactional(readOnly = true)
    fun <T> countByType(clazz: Class<T>): Long {
        return repository.count(clazz)
    }

    @Transactional(readOnly = true)
    fun load(outboxEntityId: OutboxEntityId): OutboxEntity {
        return repository.load(outboxEntityId)
    }

    fun addAfterCompletionListener(afterCompletionListener: AfterCompletionListener) {
        this.afterCompletionListeners.add(afterCompletionListener)
    }
}