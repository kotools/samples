package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ErrorTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns original message`() {
        val message = "An error has occurred."
        val actual: String = Error.orThrow(message)
            .toString()
        assertEquals(expected = message, actual)
    }

    // ----------------------- Companion.orNull(String) ------------------------

    @Test
    fun `orNull passes with a non-blank message`() {
        val message = "An error has occurred."
        val error: Error? = Error.orNull(message)
        assertNotNull(error)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `orNull fails with a blank message`() {
        val error: Error? = Error.orNull(" ")
        assertNull(error)
    }

    // ----------------------- Companion.orThrow(String) -----------------------

    @Test
    fun `orThrow passes with non-blank message`() {
        val message = "An error has occurred."
        val error: Error = Error.orThrow(message)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `orThrow fails with a blank message`() {
        val exception: IllegalArgumentException = assertFailsWith {
            Error.orThrow(" ")
        }
        val expected = "Blank error's message specified."
        assertEquals(expected, actual = exception.message)
    }
}
