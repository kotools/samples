package org.kotools.samples.jvm.demo

import kotlin.test.Test
import kotlin.test.assertEquals

class IntegersKotlinSample {
    @Test
    fun takeIfPositive() {
        val integer: Int = (1..Int.MAX_VALUE)
            .random()
        val actual: Int? = integer.takeIfPositive()
        assertEquals(expected = integer, actual)
    }
}
