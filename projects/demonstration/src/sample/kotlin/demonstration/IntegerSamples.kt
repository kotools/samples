package demonstration

@Suppress("unused")
class IntSample {
    fun addition() {
        val x = 1
        val y = 2
        check(x + y == 3)
    }

    fun subtraction(): Unit = check(2 - 1 == 1)
}

@Suppress("unused")
class LongSample {
    fun addition() {
        val x = 1L
        val y = 2L
        check(x + y == 3L)
    }

    fun subtraction(): Unit = check(2L - 1L == 1L)
}
