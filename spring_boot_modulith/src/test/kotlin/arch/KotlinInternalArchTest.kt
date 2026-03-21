package arch

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotlinInternalArchTest {

    @Test
    fun `should fail when internal class is used outside its package`() {
        val allClasses = ClassFileImporter().importPackages("arch.scenario1")

        val rule1: ArchRule = classes().that(isKotlinInternal()).should(notBeCalledFromOutsidePackage())
        rule1.check(allClasses)
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
        assertTrue {
            sce.descriptionLines[0].startsWith("Access to self: JavaClass{name='arch.scenario1.ClassCallAInternal'} is in outer package than JavaClass{name='arch.scenario1.a.AInternal'}")
        }
    }
}