package org.kotools.samples.jvm.demo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class IntegersTest {
    // ------------------------- Int.takeIfPositive() --------------------------

    @Test
    fun `takeIfPositive with positive Int`() {
        val integer: Int = (1..Int.MAX_VALUE)
            .random()
        val actual: Int? = integer.takeIfPositive()
        assertEquals(expected = integer, actual)
    }

    @Test
    fun `takeIfPositive with Int that equals zero`() {
        val actual: Int? = 0.takeIfPositive()
        assertNull(actual)
    }

    @Test
    fun `takeIfPositive with negative Int`() {
        val integer: Int = (Int.MIN_VALUE..-1)
            .random()
        val actual: Int? = integer.takeIfPositive()
        assertNull(actual)
    }
}
