package promitech.ecc.lock

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import promitech.ecc.TransactionService
import javax.persistence.EntityManager
import javax.persistence.LockModeType

@Transactional
class LockService(
    private val entityManager: EntityManager,
    private val transactionService: TransactionService,
    private val lockTimeout: String
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> lock(key: Key, action: () -> T ): T {
        val entity = KeyLockEntity(key = key)
        transactionService.runInNewTransaction {
            entityManager.persist(entity)
        }
        entityManager.createQuery("select kl from KeyLockEntity kl where kl.key = :key")
            .setHint("javax.persistence.lock.timeout", lockTimeout)
            .setParameter("key", key)
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .resultList
        try {
            return action.invoke()
        } finally {
            entityManager.createQuery("delete from KeyLockEntity kl where kl.id = :id")
                .setParameter("id", entity.id)
                .executeUpdate()
        }
    }

}