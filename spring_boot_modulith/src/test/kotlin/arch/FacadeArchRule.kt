package arch

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.EvaluationResult
import com.tngtech.archunit.lang.Priority
import com.tngtech.archunit.lang.SimpleConditionEvent
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import kotlin.collections.iterator

fun JavaClass.isAnySpringBean(): Boolean {
    return this.isAnnotatedWith(Service::class.java)
            || this.isAnnotatedWith(Component::class.java)
            || this.isAnnotatedWith(Repository::class.java)
}

fun JavaClass.isFacade(): Boolean = this.name.endsWith("Facade")

class FacadeArchRule: ArchRule {
    override fun check(classes: JavaClasses) {
        ArchRule.Assertions.check(this, classes)
    }

    override fun because(reason: String?): ArchRule? {
        TODO("Not yet implemented")
    }

    override fun allowEmptyShould(allowEmptyShould: Boolean): ArchRule? {
        return this
    }

    override fun evaluate(classes: JavaClasses): EvaluationResult {
        val events = ConditionEvents.Factory.create()
        Check(classes).check(events)
        return EvaluationResult(this, events, Priority.MEDIUM)
    }

    override fun getDescription(): String? {
        return "Classes protected by facade should not be called from outside facade package"
    }

    override fun `as`(newDescription: String?): ArchRule? {
        TODO("Not yet implemented")
    }

    class Check(allClasses: JavaClasses) {
        val classesProtectedByFacades = HashMap<JavaClass, ArrayList<JavaClass>>()
        init {
            allClasses.forEach { javaClass ->
                if (javaClass.isFacade()) {
                    classesProtectedByFacades.put(javaClass, ArrayList())
                }
            }
            allClasses.forEach { javaClass ->
                for ((facadeJavaClass, list) in classesProtectedByFacades) {
                    if (!javaClass.isFacade() && javaClass.packageName.startsWith(facadeJavaClass.packageName)) {
                        list.add(javaClass)
                    }
                }
            }
        }

        fun check(events: ConditionEvents) {
            for ((facade, classes) in classesProtectedByFacades) {
                for (clazz in classes) {
                    if (clazz.isAnySpringBean()) {
                        for (accessToSelf in clazz.accessesToSelf) {
                            if (isOuterPackage(accessToSelf.originOwner, facade)) {
                                events.add(SimpleConditionEvent.violated(accessToSelf, "Class ${accessToSelf.originOwner} access to ${clazz} in outer package than facade ${facade}"))
                            }
                        }
                    }
                }
            }
        }

        private fun isOuterPackage(one: JavaClass, two: JavaClass): Boolean {
            return !one.packageName.startsWith(two.packageName)
        }
    }
}