package promitech.ecc.app

import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import promitech.ecc.TransactionService
import promitech.ecc.blob.BlobConfiguration
import promitech.ecc.blob.json.JsonConfiguration
import promitech.ecc.outbox.OutboxConfiguration
import javax.sql.DataSource


@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT5S")
@Import(
	BlobConfiguration::class,
	JsonConfiguration::class,
	OutboxConfiguration::class
)
class DemoApplication {

	@Bean
	fun transactionService(): TransactionService {
		return TransactionService()
	}

	@Bean
	fun lockProvider(dataSource: DataSource): LockProvider {
		return JdbcTemplateLockProvider(dataSource)
	}

}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
