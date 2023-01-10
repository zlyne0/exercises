package promitech.ecc.lock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.TransactionSystemException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import promitech.ecc.TransactionService
import java.util.concurrent.CountDownLatch
import javax.persistence.LockTimeoutException

@DataJpaTest
@EnableAutoConfiguration(exclude = [AutoConfigureTestDatabase::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = [LockService::class, TransactionService::class])
@TestPropertySource(
    locations = ["classpath:test.properties"],
    properties = [
        "spring.liquibase.change-log=classpath:db.test.changelog/key_lock_test.xml",
        "lockService.lock.timeout=5000"
    ]
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class LockServiceTest {

    @Autowired
    private lateinit var lockService: LockService

    @Test
    fun `should create lock and block and invoke in proper order`() {
        // given
        var str = ""
        val key = Key("some key")
        val countDownLatch1 = CountDownLatch(1)
        val countDownLatch2 = CountDownLatch(1)

        // when
        val threadOne = Thread(Runnable {
            countDownLatch1.await()
            lockService.lock(key, {
                countDownLatch2.countDown()
                sleep(1000)
                // sleep to be sure thread two create lock,
                // when broken lockservice implementation then crate string "twoone"
                str += "one"
            })
        })
        val threadTwo = Thread(Runnable {
            countDownLatch1.countDown()
            countDownLatch2.await()
            lockService.lock(key, {
                str += "two"
            })
        })

        threadOne.start()
        threadTwo.start()

        threadOne.join()
        threadTwo.join()

        // then
        assertThat(str).isEqualTo("onetwo")
    }

    @Test
    fun `should not block when create lock for different keys `() {
        // given
        var str = ""
        val key1 = Key("some key1")
        val key2 = Key("some key2")
        val countDownLatch1 = CountDownLatch(1)
        val countDownLatch2 = CountDownLatch(1)

        // when
        val threadOne = Thread(Runnable {
            countDownLatch1.await()
            lockService.lock(key1, {
                countDownLatch2.countDown()
                sleep(1000)
                // sleep to be sure thread two create lock and add value to string,
                // when broken lockservice implementation then crate string "onetwo"
                str += "one"
            })
        })
        val threadTwo = Thread(Runnable {
            countDownLatch1.countDown()
            countDownLatch2.await()
            lockService.lock(key2, {
                str += "two"
            })
        })

        threadOne.start()
        threadTwo.start()

        threadOne.join()
        threadTwo.join()

        // then
        assertThat(str).isEqualTo("twoone")
    }

    @Test
    fun `should throw exception when lock timeout`() {
        // given
        val key = Key("some key")
        val countDownLatch1 = CountDownLatch(1)
        val countDownLatch2 = CountDownLatch(1)

        // when
        val threadOne = Thread(Runnable {
            countDownLatch1.await()
            lockService.lock(key, {
                countDownLatch2.countDown()
                sleep(6000)
                // sleep longer then timeout
            })
        })
        var exception: TransactionSystemException? = null
        val threadTwo = Thread(Runnable {
            countDownLatch1.countDown()
            countDownLatch2.await()
            try {
                lockService.lock(key, {
                })
            } catch (e: TransactionSystemException) {
                exception = e
            }
        })

        threadOne.start()
        threadTwo.start()

        threadOne.join()
        threadTwo.join()

        // then
        assertThat(exception).isNotNull()
        assertThat(exception!!.applicationException).isInstanceOf(LockTimeoutException::class.java)
    }

    @Test
    fun `should remove entity record while exception`() {
        // given
        val key = Key("some key")

        // when
        var exception: ApplicationLogicException? = null
        try {
            lockService.lock(key, {
                throw ApplicationLogicException()
            })
        } catch (e: ApplicationLogicException) {
            exception = e
        }

        // then
        assertThat(exception).isInstanceOf(ApplicationLogicException::class.java)
    }

    fun sleep(milisec: Long) {
        Thread.sleep(milisec)
    }

    class ApplicationLogicException: RuntimeException()
}