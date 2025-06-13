package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ExceptionMessageTest {
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
