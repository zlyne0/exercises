package demo.b

import org.springframework.stereotype.Service

@Service
class BService {
    fun echo() {
        println("Echo from BService")
        //KObject().someKFunctionality()
    }
}