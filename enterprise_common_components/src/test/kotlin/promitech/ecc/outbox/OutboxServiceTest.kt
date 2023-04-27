package promitech.ecc.outbox

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import promitech.ecc.BaseITTest
import promitech.ecc.TransactionService
import java.lang.IllegalArgumentException
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

    var sendMessageActionCompleted = false

    @Test
    fun `should persist and send message`() {
        // given
        registerAfterSendMessageActionCompleted()

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

        waitForSendMessageActionCompleted()

        // then
        assertThat(runOrder).describedAs("action should run async").isEqualTo("onetwo")
        assertThat(entityCountOnAction).isEqualTo(countBeforeAction+1)

        val countAfterAction = outboxService.countByType(Contract::class.java)
        assertThat(countBeforeAction)
            .describedAs("should delete entity after successful action")
            .isEqualTo(countAfterAction)
    }

    @Test
    fun `should increase message send error count when exception on processing message`() {
        // given
        registerAfterSendMessageActionCompleted()

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
        waitForSendMessageActionCompleted()

        // then
        val countAfterAction = outboxService.countByType(Contract::class.java)
        assertThat(countBeforeAction + 1)
            .describedAs("should not delete entity when error on action")
            .isEqualTo(countAfterAction)

        val outboxEntity = transactionService.runInNewTransaction { outboxService.load(outboxEntityId) }
        assertThat(outboxEntity.errorCount).isEqualTo(1)
        assertThat(outboxEntity.lastErrorDate).isNotNull()
    }

    @Test
    fun `should throw exception when can not find send handler for message`() {
        // given
        // no action registry
        val contract = Contract("contractNumber1234", "contractProductName", 1003)

        // when
        val exception = Assertions.assertThrows(IllegalArgumentException::class.java, {
            transactionService.runInNewTransaction {
                outboxService.send(contract)
            }
        })

        // then
        assertThat(exception).hasMessageStartingWith("can not find handler configuration for")
    }

    fun registerAfterSendMessageActionCompleted() {
        outboxService.addAfterCompletionListener(object: OutboxService.AfterCompletionListener {
            override fun action(outboxEntityId: Long) {
                sendMessageActionCompleted = true
            }
        })
    }

    fun waitForSendMessageActionCompleted() {
        Awaitility.with()
            .pollInterval(Duration.ofMillis(100))
            .await()
            .atMost(Duration.ofMillis(1500))
            .until { sendMessageActionCompleted }
    }

}