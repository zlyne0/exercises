package promitech.ecc.outbox

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import promitech.ecc.BaseITTest
import promitech.ecc.TransactionService
import java.lang.IllegalStateException
import java.time.Duration
import java.util.concurrent.CountDownLatch

data class Contract(
    val number: String,
    val name: String,
    val value: Int
)

@ContextConfiguration(classes = [
    TransactionService::class,
    OutboxConfiguration::class
])
@TestPropertySource(properties = [
    "spring.liquibase.change-log=classpath:db.test.changelog/outbox_test.xml",
])
class OutboxServiceTest: BaseITTest() {

    @Autowired
    private lateinit var outboxService: OutboxService

    @Autowired
    private lateinit var transactionService: TransactionService

    @Autowired
    private lateinit var outboxRegistryService: OutboxRegistryService

    @Test
    fun `should persist and send message`() {
        // given
        val contract = Contract("contractNumber1234", "contractProductName", 1003)
        var runOrder = ""
        val countDownLatch1 = CountDownLatch(1)
        var entityCountOnAction = 0L

        outboxRegistryService.registry(Contract::class.java, object: OutboxAction<Contract> {
            override fun action(data: Contract) {
                countDownLatch1.await()
                runOrder += "two"
                entityCountOnAction = outboxService.countByType(Contract::class.java)
            }
        })

        val countBeforeAction = outboxService.countByType(Contract::class.java)

        // when
        transactionService.runInNewTransaction {
            outboxService.send(contract)
        }
        runOrder += "one"
        countDownLatch1.countDown()

        Awaitility.with()
            .pollInterval(Duration.ofMillis(100))
            .await()
            .atMost(Duration.ofMillis(500))
            .until { countAfterTheSameAsBefore(countBeforeAction) }

        // then
        assertThat(runOrder).describedAs("action should run async").isEqualTo("onetwo")
        assertThat(entityCountOnAction).isEqualTo(countBeforeAction+1)
    }

    private fun countAfterTheSameAsBefore(beforeCount: Long): Boolean {
        val count = outboxService.countByType(Contract::class.java)
        return count == beforeCount
    }

    //@Test
    fun `should increase message send error count when exception on processing message`() {
        // given
        outboxRegistryService.registry(Contract::class.java, object: OutboxAction<Contract> {
            override fun action(data: Contract) {
                throw IllegalStateException("test exception in action")
            }
        })

        val contract = Contract("contractNumber1234", "contractProductName", 1003)
        val countBeforeAction = outboxService.countByType(Contract::class.java)

        // when
        val outboxEntityId = transactionService.runInNewTransaction {
            outboxService.send(contract)
        }

        // then
        val countAfterAction = outboxService.countByType(Contract::class.java)
        assertThat(countBeforeAction + 1).isEqualTo(countAfterAction)
        //load outbox id and check counter

        val outboxEntity = transactionService.runInNewTransaction { outboxService.load(outboxEntityId) }
        assertThat(outboxEntity.errorCount).isEqualTo(1)
        assertThat(outboxEntity.lastErrorDate).isNotNull()
    }

    //@Test
    fun `should throw exception when can not find send handler for message`() {
        // given

        // when

        // then
    }
}