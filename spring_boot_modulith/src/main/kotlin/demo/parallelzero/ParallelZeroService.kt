package demo.parallelzero

import demo.c.CService
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