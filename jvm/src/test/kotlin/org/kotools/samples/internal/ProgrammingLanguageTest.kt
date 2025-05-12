package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProgrammingLanguageCompanionTest {
    // ----------------------------- orNull(File) ------------------------------

    @Test
    fun `orNull should pass with file having 'java' extension`() {
        val file = File("Hello.java")
        val actual: ProgrammingLanguage? = ProgrammingLanguage.orNull(file)
        val expected: ProgrammingLanguage = Java()
        assertEquals(expected, actual)
    }

    @Test
    fun `orNull should pass with file having 'kt' extension`() {
        val file = File("Hello.kt")
        val actual: ProgrammingLanguage? = ProgrammingLanguage.orNull(file)
        val expected: ProgrammingLanguage = Kotlin()
        assertEquals(expected, actual)
    }

    @Test
    fun `orNull should fail with unsupported file`() {
        val file = File("Unsupported.txt")
        val actual: ProgrammingLanguage? = ProgrammingLanguage.orNull(file)
        assertNull(actual)
    }
}
