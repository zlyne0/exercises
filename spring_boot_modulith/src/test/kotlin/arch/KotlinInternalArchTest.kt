package arch

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotlinInternalArchTest {

    @Test
    fun `should fail when internal class is used outside its package`() {
        val allClasses = ClassFileImporter().importPackages("arch.scenario1")
        val kotlinInternalOutsidePackage: ArchRule = classes().that(isKotlinInternal()).should(notBeCalledFromOutsidePackage())

        val exception: AssertionError = assertThrows {
            kotlinInternalOutsidePackage.check(allClasses)
        }
        exception.printStackTrace()
        assertThat(exception).hasMessageStartingWith("Architecture Violation [Priority: MEDIUM] - Rule 'classes that Kotlin internal class should not be called from outside package' was violated (2 times):\n" +
                "Access to self: JavaClass{name='arch.scenario1.a.AInternal'} is in outer package JavaClass{name='arch.scenario1.ClassCallAInternal'}\n" +
                "Access to self: JavaClass{name='arch.scenario1.a.AInternal'} is in outer package JavaClass{name='arch.scenario1.ClassCallAInternal'}")
    }

    @Test
    fun `should find classes called from outside`() {
        // given
        val allClasses = ClassFileImporter().importPackages("arch.scenario1")

        val eventsStore = MySimpleConditionEvents()
        val isKotlinInternalPredicate = isKotlinInternal()
        val notBeCalledFromOutsidePackageCondition = notBeCalledFromOutsidePackage()

        // when
        allClasses.asSequence()
            .filter { clazz -> isKotlinInternalPredicate.test(clazz) }
            .forEach { clazz -> notBeCalledFromOutsidePackageCondition.check(clazz, eventsStore) }

        // then
        assertEquals(2, eventsStore.violating.size)
        val sce = eventsStore.violating.first() as SimpleConditionEvent
        println(sce.descriptionLines[0])
        assertTrue {
            sce.descriptionLines[0].startsWith("Access to self: JavaClass{name='arch.scenario1.a.AInternal'} is in outer package JavaClass{name='arch.scenario1.ClassCallAInternal'}")
        }
    }
}