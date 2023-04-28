package promitech.ecc.outbox

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import promitech.ecc.blob.json.JsonObjectId
import javax.persistence.EntityManager
import javax.persistence.LockModeType

@Transactional(propagation = Propagation.MANDATORY)
class OutboxRepository(
    private val entityManager: EntityManager
) {

    fun create(jsonObjectId: JsonObjectId, type: String): OutboxEntity {
        val entity = OutboxEntity(jsonObjectId = jsonObjectId, type = type)
        entityManager.persist(entity)
        return entity
    }

    fun findAndLock(outboxEntityId: Long, lockTimeout: String): OutboxEntity? {
        val list = entityManager.createQuery("select oe from OutboxEntity oe where oe.id = :id")
            .setHint("javax.persistence.lock.timeout", lockTimeout)
            .setParameter("id", outboxEntityId)
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .resultList
        if (list.isEmpty()) {
            return null
        }
        return list.get(0) as OutboxEntity
    }

    fun delete(outboxEntityId: Long) {
        entityManager.createQuery("delete from OutboxEntity oe where oe.id = :id")
            .setParameter("id", outboxEntityId)
            .executeUpdate()
    }

    fun load(outboxEntityId: OutboxEntityId): OutboxEntity {
        return entityManager.createQuery("select oe from OutboxEntity oe where oe.id = :id")
            .setParameter("id", outboxEntityId.value)
            .singleResult as OutboxEntity
    }

    fun <T> count(clazz: Class<T>): Long {
        val count = entityManager.createQuery("select count(oe) from OutboxEntity oe where oe.type = :type")
            .setParameter("type", clazz.name)
            .singleResult
        return count as Long
    }

}