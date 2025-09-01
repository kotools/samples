package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorTest {
    // -------------------------- constructor(String) --------------------------

    @Test
    fun `constructor passes with non-blank message`() {
        val message = "Error found."
        val error = Error(message)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `constructor fails with blank message`() {
        val exception: IllegalArgumentException = assertFailsWith { Error(" ") }
        val expected = "Blank error message specified."
        assertEquals(expected, actual = exception.message)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns message`() {
        val error = Error("Error found.")
        val actual: String = error.toString()
        assertEquals(expected = error.message, actual)
    }

    // ----------------------- Companion.orThrow(String) -----------------------

    @Test
    fun `orThrow passes with non-blank message`() {
        val message = "Error found."
        val error: Error = Error.orThrow(message)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `orThrow fails with blank message`() {
        val exception: IllegalArgumentException = assertFailsWith {
            Error.orThrow(" ")
        }
        val expected = "Blank error's message specified."
        assertEquals(expected, actual = exception.message)
    }
}
