package arch.scenario3.c

import arch.scenario3.a.AAComponent
import arch.scenario3.b.BBComponent

class CCComponent {

    fun echo(bbComponent: BBComponent) {
        println("CCComponent echoing: " + bbComponent.getMessage())
    }

    fun getMessage(): String {
        return "Hello from CCComponent!"
    }

}