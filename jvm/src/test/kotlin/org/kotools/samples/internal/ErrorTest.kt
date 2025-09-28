package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorTest {
    // --------------------------- String.toError() ----------------------------

    @Test
    fun toErrorPassesOnNonBlankString() {
        val message = "Error found."
        val error: Error = message.toError()
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun toErrorFailsOnBlankString() {
        val exception: IllegalArgumentException =
            assertFailsWith(block = " "::toError)
        val expected = "Blank error's message specified."
        assertEquals(expected, actual = exception.message)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns message`() {
        val error: Error = Error.orThrow("Error found.")
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
