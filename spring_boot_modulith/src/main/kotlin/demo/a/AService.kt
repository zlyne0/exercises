package demo.a

import demo.b.BService
import org.springframework.stereotype.Service

@Service
class AService(
    val bService: BService
) {
    fun echo() {
        bService.echo()
    }
}