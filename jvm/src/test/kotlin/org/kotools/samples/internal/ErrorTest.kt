package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
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
        assertNotNull(error)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `orNull fails with blank message`() {
        val error: Error? = Error.orNull("   ")
        assertNull(error)
    }

    // ----------------------- Companion.orThrow(String) -----------------------

    @Test
    fun `orThrow passes with non-blank message`() {
        val message = "Error found."
        val error = Error.orThrow(message)
        assertEquals(expected = message, actual = error.message)
    }

    @Test
    fun `orThrow fails with blank message`() {
        val exception: IllegalArgumentException = assertFailsWith {
            Error.orThrow(" ")
        }
        val expected = "Blank error message."
        assertEquals(expected, actual = exception.message)
    }

    // ---------------- Companion.multipleClassesFoundIn(File) -----------------

    @Test
    fun `multipleClassesFoundIn returns meaningful message`() {
        val file = File("Sample.kt")
        val error: Error = Error.multipleClassesFoundIn(file)
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, error.message)
    }

    // ----------------- Companion.noPublicClassFoundIn(File) ------------------

    @Test
    fun `noPublicClassFoundIn returns meaningful message`() {
        val file = File("Sample.kt")
        val error: Error = Error.noPublicClassFoundIn(file)
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual = error.message)
    }

    // --------- Companion.singleExpressionKotlinFunctionFoundIn(File) ---------

    @Test
    fun `singleExpressionKotlinFunctionFoundIn returns meaningful message`() {
        val file = File("Sample.kt")
        val error: Error = Error.singleExpressionKotlinFunctionFoundIn(file)
        val expected = "Single-expression Kotlin function found in '$file'."
        assertEquals(expected, actual = error.message)
    }
}
