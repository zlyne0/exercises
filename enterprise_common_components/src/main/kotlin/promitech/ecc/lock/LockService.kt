package promitech.ecc.lock

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import promitech.ecc.TransactionService
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext

@Service
@Transactional
class LockService(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val transactionService: TransactionService,
    @Value("\${lockService.lock.timeout}")
    private val lockTimeout: String
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> lock(key: Key, action: () -> T ): T {
        val entity = KeyLockEntity(key = key)
        transactionService.runInNewTransaction {
            entityManager.persist(entity)
        }
        try {
            entityManager.createQuery("select kl from KeyLockEntity kl where kl.key = :key")
                .setHint("javax.persistence.lock.timeout", lockTimeout)
                .setParameter("key", key)
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .resultList
        } catch (e: RuntimeException) {
            throw e
        }
        try {
            return action.invoke()
        } finally {
            entityManager.createQuery("delete from KeyLockEntity kl where kl.id = :id")
                .setParameter("id", entity.id)
                .executeUpdate()
        }
    }

}