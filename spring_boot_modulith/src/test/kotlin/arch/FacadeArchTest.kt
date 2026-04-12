package arch

import arch.scenario2.a.AComponent
import arch.scenario2.a.AFacade
import arch.scenario2.a.b.BInternalAClient
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class FacadeArchTest {

    // jesli w package wystepuje Facade to nie moze byc zadnych innych beanow springowych uzywanych z tego package
    //   publiczne moga byc ale tylko uzywane w podpakietach
    @Test
    fun `should fail because AComponent is called from AClientComponent and AFacade exists`() {
        val allClasses = ClassFileImporter().importPackages("arch.scenario2")
        val facadeArchRule: ArchRule = FacadeArchRule()

        val exception: AssertionError = assertThrows {
            facadeArchRule.check(allClasses)
        }
        exception.printStackTrace()
        assertThat(exception).hasMessageStartingWith("Architecture Violation [Priority: MEDIUM] - Rule 'Classes protected by facade should not be called from outside facade package' was violated (1 times):\n" +
                "Class JavaClass{name='arch.scenario2.AClientComponent'} access to JavaClass{name='arch.scenario2.a.AComponent'} in outer package than facade JavaClass{name='arch.scenario2.a.AFacade'}")
    }

    @Test
    fun `should create facades package classes`() {
        // given
        val allClasses = ClassFileImporter().importPackages("arch.scenario2")

        // when
        val checkFacadesPackages = FacadeArchRule.Check(allClasses)

        // then
        assertEquals(1, checkFacadesPackages.classesProtectedByFacades.size)
        for ((facade, clazzList) in checkFacadesPackages.classesProtectedByFacades) {
            assertEquals(AFacade::class.java.name, facade.name)
            assertContains(AComponent::class.java, clazzList)
            assertContains(BInternalAClient::class.java, clazzList)
        }
    }

    fun assertContains(clazz: Class<out Any>, list: List<JavaClass>,) {
        for (javaClass in list) {
            if (javaClass.name.equals(clazz.name)) {
                return
            }
        }
        fail("Can not find class $clazz")
    }
}