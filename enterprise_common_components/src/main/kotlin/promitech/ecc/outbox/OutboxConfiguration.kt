package promitech.ecc.outbox

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import promitech.ecc.TransactionService
import promitech.ecc.blob.json.JsonConfiguration
import promitech.ecc.blob.json.JsonService
import java.time.Clock
import javax.persistence.EntityManager

@Configuration
@Import(JsonConfiguration::class)
@EntityScan(basePackageClasses = [OutboxConfiguration::class])
class OutboxConfiguration {

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    internal fun outboxRepository(
        entityManager: EntityManager
    ): OutboxRepository {
        return OutboxRepository(entityManager)
    }

    @Bean
    internal fun outboxRegistryService(): OutboxRegistryService {
        return OutboxRegistryService()
    }

    @Bean
    internal fun outboxService(
        entityManager: EntityManager,
        jsonService: JsonService,
        transactionService: TransactionService,
        outboxRegistryService: OutboxRegistryService,
        @Value("\${ecc.outbox.lock.timeout:5000}")
        lockTimeout: String,
        clock: Clock
    ): OutboxService {
        return OutboxService(entityManager, jsonService, transactionService, outboxRegistryService, lockTimeout, clock)
    }

}