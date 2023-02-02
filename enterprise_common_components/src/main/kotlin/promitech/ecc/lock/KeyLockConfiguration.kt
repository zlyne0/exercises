package promitech.ecc.lock

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import promitech.ecc.TransactionService
import javax.persistence.EntityManager

@Configuration
@EntityScan(basePackageClasses = [KeyLockConfiguration::class])
@EnableJpaRepositories(basePackageClasses = [KeyLockConfiguration::class])
class KeyLockConfiguration {

    @Bean
    fun lockService(
        entityManager: EntityManager,
        transactionService: TransactionService,
        @Value("\${lockService.lock.timeout:5000}")
        lockTimeout: String
    ): LockService {
        return LockService(entityManager, transactionService, lockTimeout)
    }

}