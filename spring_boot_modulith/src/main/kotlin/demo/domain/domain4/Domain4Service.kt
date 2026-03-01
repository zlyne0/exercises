package demo.domain.domain4

import demo.domain.domain4.domain5.Domain5Service

class Domain4Service {
    fun echo(message: String): String {
        Domain5Service().echo(message)
        return "Domain4Service: $message"
    }
}