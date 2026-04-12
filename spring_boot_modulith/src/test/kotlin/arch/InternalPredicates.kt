@file:JvmName("InternalPredicates")

package arch

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import kotlin.reflect.KVisibility

fun notBeCalledFromOutsidePackage(): ArchCondition<JavaClass> {
    return object: ArchCondition<JavaClass>("not be called from outside package") {
        override fun check(
            item: JavaClass,
            events: ConditionEvents
        ) {
            for (accessToSelf in item.accessesToSelf) {
                if (isOuterPackage(accessToSelf.originOwner, item)) {
                    events.add(SimpleConditionEvent.violated(accessToSelf, "Access to self: ${item} is in outer package ${accessToSelf.originOwner}"))
                }
            }
        }

        fun isOuterPackage(one: JavaClass, two: JavaClass): Boolean {
            return !one.packageName.startsWith(two.packageName)
        }
    }
}

fun isKotlinInternal() = object : DescribedPredicate<JavaClass>("Kotlin internal class") {
    override fun test(input: JavaClass): Boolean {
        return input.reflect().isInternal()
    }
}

fun isKotlinNotInternal() = object : DescribedPredicate<JavaClass>("Kotlin not-internal class") {
    override fun test(input: JavaClass): Boolean {
        val clazz = input.reflect()
        return clazz.isKotlinClass() && !clazz.isInternal()
    }
}

fun Class<*>.isInternal() = this.kotlin.visibility == KVisibility.INTERNAL

private fun Class<*>.isKotlinClass() = this.declaredAnnotations.any {
    it.annotationClass.qualifiedName == "kotlin.Metadata"
}