package arch.scenario3.b

import arch.scenario3.a.AAComponent

class BBComponent {

    fun echo(aaComponent: AAComponent) {
        println("BBComponent echoing: " + aaComponent.getMessage())
    }

    fun getMessage(): String {
        return "Hello from BBComponent!"
    }

}