package arch.scenario3.a

import arch.scenario3.c.CCComponent

class AAComponent {

    fun echo(ccComponent: CCComponent) {
        println("AAComponent echoing: " + ccComponent.getMessage())
    }

    fun getMessage(): String {
        return "Hello from AAComponent!"
    }
}