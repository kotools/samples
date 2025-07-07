package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExceptionMessageTest {
    // ----------------------------- equals(Any?) ------------------------------

    @Test
    fun `equals passes with same exception message`() {
        val text = "Failed requirement."
        val message: ExceptionMessage = ExceptionMessage.orThrow(text)
        val other: ExceptionMessage = ExceptionMessage.orThrow(text)
        val actual: Boolean = message == other
        assertTrue(actual)
    }

    @Test
    fun `equals fails with another exception message`() {
        val message: ExceptionMessage =
            ExceptionMessage.orThrow("First problem.")
        val other: ExceptionMessage =
            ExceptionMessage.orThrow("Second problem.")
        val actual: Boolean = message == other
        assertFalse(actual)
    }

    @Test
    fun `equals fails with another type than ExceptionMessage`() {
        val message: ExceptionMessage =
            ExceptionMessage.orThrow("Failed requirement.")
        val other = "oops"
        val actual: Boolean = message.equals(other)
        assertFalse(actual)
    }

    // ------------------------------ hashCode() -------------------------------

    @Test
    fun `hashCode passes`() {
        val text = "Failed requirement."
        val actual: Int = ExceptionMessage.orThrow(text)
            .hashCode()
        val expected: Int = text.hashCode()
        assertEquals(expected, actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val expected = "Failed requirement."
        val actual: String = ExceptionMessage.orThrow(expected)
            .toString()
        assertEquals(expected, actual)
    }

    // ----------------------- Companion.orThrow(String) -----------------------

    @Test
    fun `orThrow passes with non-blank text`() {
        val text = "Failed requirement."
        ExceptionMessage.orThrow(text)
    }

    @Test
    fun `orThrow fails with blank text`() {
        val text = "   "
        val exception: IllegalArgumentException = assertFailsWith {
            ExceptionMessage.orThrow(text)
        }
        val actual: String? = exception.message
        val expected = "Blank exception message ('$text')."
        assertEquals(expected, actual)
    }

    // ---------------- Companion.multipleClassesFoundIn(File) -----------------

    @Test
    fun `multipleClassesFoundIn returns meaningful error message`() {
        val file = File("Sample.kt")
        val actual: String = ExceptionMessage.multipleClassesFoundIn(file)
            .toString()
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    // ----------------- Companion.noPublicClassFoundIn(File) ------------------

    @Test
    fun `noPublicClassFoundIn returns meaningful error message`() {
        val file = File("Sample.kt")
        val actual: String = ExceptionMessage.noPublicClassFoundIn(file)
            .toString()
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual)
    }
}
