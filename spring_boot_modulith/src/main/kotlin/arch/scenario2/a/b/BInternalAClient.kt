package arch.scenario2.a.b

import arch.scenario2.a.AComponent
import org.springframework.stereotype.Component

@Component
class BInternalAClient(val aComponent: AComponent) {
}