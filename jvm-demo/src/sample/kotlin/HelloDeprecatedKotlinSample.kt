import kotlin.test.Test
import kotlin.test.assertEquals

class HelloDeprecatedKotlinSample {
    @Test
    fun greet() {
        val name = "Sample"
        val greeting: String = greet(name)
        val expected = "Hello $name!"
        assertEquals(expected, greeting)
    }
}
