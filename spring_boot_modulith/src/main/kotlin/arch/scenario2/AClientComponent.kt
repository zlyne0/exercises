package arch.scenario2

import arch.scenario2.a.AComponent
import arch.scenario2.a.ADto
import arch.scenario2.c.CComponent
import org.springframework.stereotype.Component

@Component
class AClientComponent(
    val aComponent: AComponent,
    val cComponent: CComponent
) {
    fun echo() {
        println(ADto("one", "two"))
        println("" + aComponent.echo())
        println("" + cComponent)
    }
}