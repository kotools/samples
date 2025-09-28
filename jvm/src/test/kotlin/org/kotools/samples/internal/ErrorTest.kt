package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns message`() {
        val error = Error("Error found.")
        val actual: String = error.toString()
        assertEquals(expected = error.message, actual)
    }
}
