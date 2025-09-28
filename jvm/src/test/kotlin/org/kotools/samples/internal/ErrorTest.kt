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
        val error: Error = "Error found.".toError()
        val actual: String = error.toString()
        assertEquals(expected = error.message, actual)
    }
}
