package promitech.ecc.outbox

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Transactional(propagation = Propagation.MANDATORY)
class OutboxRepository(
    private val entityManager: EntityManager
) {

    fun save(outboxEntity: OutboxEntity) {
        entityManager.persist(outboxEntity)
    }

}