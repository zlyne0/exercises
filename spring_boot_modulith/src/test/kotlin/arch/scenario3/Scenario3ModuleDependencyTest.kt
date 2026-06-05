package arch.scenario3

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class Scenario3ModuleDependencyTest {

    @Test
    fun `should detect cycle between modules`() {
        // given
        val modules = ApplicationModules.of("arch.scenario3")
        val myModules = MyApplicationModules(modules)
//        println(modules)

        Assertions.assertThatThrownBy {
            // when
            myModules.verify()
//            modules.verify()
        }.hasMessageStartingWith("- Cycle detected:")
    }

}