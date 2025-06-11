package sample

import kotlin.test.Test
import kotlin.test.assertTrue

class SinglePublicClassWithPackageSample {
    @Test
    fun isPositive() {
        val number: Int = (1..Int.MAX_VALUE)
            .random()
        assertTrue(number > 0)
    }
}
