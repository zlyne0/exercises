package demo.zero

import demo.c.CService
import demo.a.AService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
class ZeroServiceTest {

//    @MockitoBean
//    lateinit var aService: AService

//    @MockitoBean
//    lateinit var bService: BService

//    @MockitoBean
//    lateinit var cService: CService
//
    @Autowired
    lateinit var zeroService: ZeroService

    @Test
    fun `name`() {
        // given
        // when
        zeroService.echo()

        // then
    }
}