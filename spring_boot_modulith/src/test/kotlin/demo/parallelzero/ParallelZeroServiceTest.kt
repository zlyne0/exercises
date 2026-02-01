package demo.parallelzero

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.modulith.test.ApplicationModuleTest

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
class ParallelZeroServiceTest {

    @Autowired
    lateinit var parallelZeroService: ParallelZeroService

    @Test
    fun `name`() {
        // given
        parallelZeroService.echo()

        // when

        // then
    }

}