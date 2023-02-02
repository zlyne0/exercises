package promitech.ecc.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement
import promitech.ecc.blob.BlobConfiguration
import promitech.ecc.blob.json.JsonConfiguration

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@Import(
	BlobConfiguration::class,
	JsonConfiguration::class
)
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
