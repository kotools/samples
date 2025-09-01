package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ErrorTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns message`() {
        val error: Error = Error.orThrow("Error found.")
        val actual: String = error.toString()
        assertEquals(expected = error.message, actual)
    }

    // ----------------------- Companion.orNull(String) ------------------------

    @Test
    fun `orNull passes with non-blank message`() {
        val message = "Error found."
        val error: Error? = Error.orNull(message)
        assertEquals(expected = message, actual = error?.message)
    }

    @Test
    fun `orNull fails with blank message`() {
        val error: Error? = Error.orNull(" ")
        assertNull(error)
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
