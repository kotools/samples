import kotlin.test.Test
import kotlin.test.assertTrue

class SinglePublicClassSample {
    @Test
    fun isPositive() {
        val number: Int = (1..Int.MAX_VALUE)
            .random()
        assertTrue(number > 0)
    }
}
