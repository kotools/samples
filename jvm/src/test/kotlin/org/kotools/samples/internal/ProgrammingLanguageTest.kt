package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProgrammingLanguageTest {
    @Test
    fun `constructor-like function should pass with Java file`() {
        val file = File("Hello.java")
        ProgrammingLanguage(file)
    }

    @Test
    fun `constructor-like function should pass with Kotlin file`() {
        val file = File("Hello.kt")
        ProgrammingLanguage(file)
    }

    @Test
    fun `constructor-like function should fail with unsupported file`() {
        val file = File("Unsupported.txt")
        val actual: String? = assertFailsWith<IllegalArgumentException> {
            ProgrammingLanguage(file)
        }.message
        val expected = "'.${file.extension}' files are not supported."
        assertEquals(expected, actual)
    }
}
