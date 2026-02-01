package demo.zero

import demo.domain.Money
import demo.domain.Pesel
import demo.c.CService
import demo.a.AService
import org.springframework.stereotype.Service

@Service
class ZeroService(
    val aService: AService,
    val cService: CService
) {
    fun echo() {
        aService.echo()
        cService.echo()

        Pesel()
        Money()

    }
}