package demo.parallelzero

import demo.a.AService
import demo.c.CService
import demo.domain.Money
import demo.domain.Pesel
import demo.e.EService
import org.springframework.stereotype.Service

@Service
class ParallelZeroService(
    val eService: EService,
    val cService: CService
) {
    fun echo() {
        eService.echo()
        cService.echo()
    }
}