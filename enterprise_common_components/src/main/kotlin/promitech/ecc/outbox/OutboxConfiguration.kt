package promitech.ecc.outbox

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import promitech.ecc.TransactionService
import promitech.ecc.blob.json.JsonConfiguration
import promitech.ecc.blob.json.JsonService
import java.time.Clock
import java.time.Duration
import javax.persistence.EntityManager

@Configuration
@Import(JsonConfiguration::class)
@EntityScan(basePackageClasses = [OutboxConfiguration::class])
class OutboxConfiguration {

    val reprocessEntitiesDuration = Duration.ofMinutes(5)

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    internal fun outboxRepository(
        entityManager: EntityManager,
        clock: Clock,
    ): OutboxRepository {
        return OutboxRepository(entityManager, clock, reprocessEntitiesDuration)
    }

    @Bean
    internal fun outboxRegistryService(): OutboxRegistryService {
        return OutboxRegistryService()
    }

    @Bean
    internal fun outboxService(
        outboxRepository: OutboxRepository,
        jsonService: JsonService,
        transactionService: TransactionService,
        outboxRegistryService: OutboxRegistryService,
        @Value("\${ecc.outbox.lock.timeout:5000}")
        lockTimeout: String,
        clock: Clock
    ): OutboxService {
        return OutboxService(outboxRepository, jsonService, transactionService, outboxRegistryService, lockTimeout, clock)
    }

    @Bean
    @ConditionalOnProperty(value = ["ecc.outbox.scheduler.enabled"], havingValue = "true", matchIfMissing = true)
    internal fun schedulerJob(outboxService: OutboxService): SchedulerJob {
        return SchedulerJob(outboxService)
    }
}