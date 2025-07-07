package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorTest {
    // --------------------- multipleClassesFoundIn(File) ----------------------

    @Test
    fun `multipleClassesFoundIn passes`() {
        val file = File("Sample.kt")
        val actual: String? = Error.multipleClassesFoundIn(file)
            .message
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    // ---------------------- noPublicClassFoundIn(File) -----------------------

    @Test
    fun `noPublicClassFoundIn passes`() {
        val file = File("Sample.kt")
        val actual: String? = Error.noPublicClassFoundIn(file)
            .message
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual)
    }

    // -------------- singleExpressionKotlinFunctionFoundIn(File) --------------

    @Test
    fun `singleExpressionKotlinFunctionFoundIn passes`() {
        val file = File("Sample.kt")
        val actual: String? = Error.singleExpressionKotlinFunctionFoundIn(file)
            .message
        val expected = "Single-expression Kotlin function found in '$file'."
        assertEquals(expected, actual)
    }
}
