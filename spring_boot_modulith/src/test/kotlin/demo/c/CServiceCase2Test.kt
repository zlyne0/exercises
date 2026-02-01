package demo.c

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.modulith.test.ApplicationModuleTest

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
class CServiceCase2Test {

    @Autowired
    lateinit var cService: CService

    @Test
    fun `name`() {
        // given
        // when
        cService.echo()

        // then
    }

}