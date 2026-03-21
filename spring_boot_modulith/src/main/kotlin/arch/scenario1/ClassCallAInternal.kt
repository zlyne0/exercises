package arch.scenario1

import arch.scenario1.a.AInternal

class ClassCallAInternal() {
    private val aInternal: AInternal = AInternal()

    fun echo() {
        aInternal.echo()
    }
}