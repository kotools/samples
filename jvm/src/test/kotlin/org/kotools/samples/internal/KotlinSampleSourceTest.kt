package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class KotlinSampleSourceTest {
    // ---------------------------- contentError() -----------------------------

    @Test
    fun `contentError passes with no error found`() {
        val fileName = "SinglePublicClassSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val error: Error? = KotlinSampleSource.orThrow(file)
            .contentError()
        assertNull(error)
    }

    @Test
    fun `contentError fails with multiple classes found`() {
        val fileName = "MultipleClassesSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val source: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("Multiple classes found in $source.")
        assertEquals(expected, error)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val fileName = "NoPublicClassSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val source: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val error: Error? = source.contentError()
        val expected: Error = Error.orThrow("No public class found in $source.")
        assertEquals(expected, error)
    }

    @Test
    fun `contentError fails with single-expression function found`() {
        val fileName = "SingleExpressionFunctionSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val source: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("Single-expression Kotlin function found in $source.")
        assertEquals(expected, error)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.kt")
        val actual: String = KotlinSampleSource.orThrow(file)
            .toString()
        assertEquals(expected = "'$file' Kotlin sample source", actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with 'Sample' suffix in Kotlin file's name`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNotNull(source)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `orNull fails with file having another extension than 'kt'`() {
        val file = File("Sample.java")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(source)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.kt")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(source)
    }

    // ------------------------ Companion.orThrow(File) ------------------------

    @Test
    fun `orThrow passes with 'Sample' suffix in Kotlin file's name`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `orThrow fails with file having another extension than 'kt'`() {
        val file = File("Sample.java")
        val exception: IllegalArgumentException = assertFailsWith {
            KotlinSampleSource.orThrow(file)
        }
        val expected = "'$file' file extension must be 'kt'."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.kt")
        val exception: IllegalArgumentException = assertFailsWith {
            KotlinSampleSource.orThrow(file)
        }
        val expected = "'$file' file name must be suffixed by 'Sample'."
        assertEquals(expected, actual = exception.message)
    }
}
