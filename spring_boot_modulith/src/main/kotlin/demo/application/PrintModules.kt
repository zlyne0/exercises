package demo.application

import demo.DemoApplication
import org.springframework.modulith.core.ApplicationModules

fun main() {
    val modules = ApplicationModules.of(DemoApplication::class.java)
    modules.forEach(System.out::println)

    // nie wyswietla internalowych serwisow jako internal a jako public
//    > Spring beans:
//    + ….AService
//    o ….JInternalAService
//    + ….KInternalAService

}