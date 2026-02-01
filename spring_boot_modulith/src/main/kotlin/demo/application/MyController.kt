package demo.application

import demo.zero.ZeroService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController(
    val zeroService: ZeroService
) {

    @GetMapping
    fun getAll() {
        zeroService.echo()
//        println(aService.echo("aaa request"))
    }

}