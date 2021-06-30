plugins {
    `java-library`
    application
    kotlin("jvm") version "1.4.10"
}

apply {
    plugin("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

application {
    mainClass.set("main.MainKt")
}

tasks.register("myTask") {

    dependsOn("run")

    doFirst {
        println("myTask do first")
    }

    doLast {
        println("myTask do last")
    }

    println("Hello myTask")

}

tasks.register("myTaskWithMethod") {
    println("Hello its myTaskWithMethod")
    println("msg from function " + echoTaskFunction("song"))
}

fun echoTaskFunction(str : String) : String {
    return "echo $str"
}
