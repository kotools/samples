package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SampleSourceFileCompanionTest {
    @Test
    fun `orNull should pass with Java file in sample source set`() {
        val file = File("sample/java/Hello.java")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should pass with Kotlin file in sample source set`() {
        val file = File("sample/kotlin/Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file outside sample source set`() {
        val file = File("Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with unsupported file in sample source set`() {
        val file = File("sample/Unsupported.txt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orThrow should pass with Java file in sample source set`() {
        val file = File("sample/java/Hello.java")
        SampleSourceFile.orThrow(file)
    }

    @Test
    fun `orThrow should pass with Kotlin file in sample source set`() {
        val file = File("sample/kotlin/Hello.kt")
        SampleSourceFile.orThrow(file)
    }

    @Test
    fun `orThrow should fail with file outside sample source set`() {
        val file = File("Hello.kt")
        val expected = "'${file.name}' file should be in a sample source set."
        val actual: String? = assertFailsWith<IllegalArgumentException> {
            SampleSourceFile.orThrow(file)
        }.message
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow should fail with unsupported file in sample source set`() {
        val file = File("sample/Unsupported.txt")
        val expected = "'.${file.extension}' files are not supported."
        val actual: String? = assertFailsWith<IllegalArgumentException> {
            SampleSourceFile.orThrow(file)
        }.message
        assertEquals(expected, actual)
    }
}
