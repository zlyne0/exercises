package arch

import com.tngtech.archunit.lang.ConditionEvent
import com.tngtech.archunit.lang.ConditionEvents
import java.util.*

class MySimpleConditionEvents : ConditionEvents {
    private val violations = ArrayList<ConditionEvent>()

    override fun add(event: ConditionEvent) {
        violations.add(event)
    }

    override fun getInformationAboutNumberOfViolations(): Optional<String> {
        TODO("Not yet implemented")
    }

    override fun setInformationAboutNumberOfViolations(informationAboutNumberOfViolations: String) {
        TODO("Not yet implemented")
    }

    override fun getViolating(): Collection<ConditionEvent> {
        return violations
    }

    override fun containViolation(): Boolean {
        return violations.isNotEmpty()
    }
}