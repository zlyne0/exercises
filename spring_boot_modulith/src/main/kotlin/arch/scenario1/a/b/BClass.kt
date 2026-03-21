package arch.scenario1.a.b

import arch.scenario1.a.AInternal

class BClass internal constructor() {

    fun echo() {
        val aInternal = AInternal()
        aInternal.echo()
    }
}