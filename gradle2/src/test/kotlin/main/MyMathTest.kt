package main

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MyMathTest {

    @Test
    internal fun plus() {
        val myMath = MyMath()
        assert(myMath.plus(3, 5) == 8)
    }
}
