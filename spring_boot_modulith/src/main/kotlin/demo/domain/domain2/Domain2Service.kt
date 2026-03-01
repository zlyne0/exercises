package demo.domain.domain2

import demo.domain.domain2.domain3.Domain3Service

class Domain2Service {
    fun doSomething() {
        Domain3Service().doSomething()
    }
}