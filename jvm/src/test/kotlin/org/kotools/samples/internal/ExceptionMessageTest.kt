package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExceptionMessageTest {
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
