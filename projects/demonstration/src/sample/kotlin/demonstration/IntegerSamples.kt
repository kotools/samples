package demonstration

import kotlin.test.Test

class IntSample {
    @Test
    fun addition() {
        val x = 1
        val y = 2
        check(x + y == 3)
    }

    @Test
    fun subtraction(): Unit = check(2 - 1 == 1)
}

class LongSample {
    @Test
    fun addition() {
        val x = 1L
        val y = 2L
        check(x + y == 3L)
    }

    @Test
    fun subtraction(): Unit = check(2L - 1L == 1L)
}
