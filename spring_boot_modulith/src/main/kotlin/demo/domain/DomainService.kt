package demo.domain

import demo.domain.domain4.Domain4Service

class DomainService {
    fun echo(message: String): String {
        Domain4Service().echo(message)
        return "DomainService: $message"
    }
}