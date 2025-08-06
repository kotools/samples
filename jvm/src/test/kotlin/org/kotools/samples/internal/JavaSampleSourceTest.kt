package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class JavaSampleSourceTest {
    // ---------------------------- contentError() -----------------------------

    @Test
    fun `contentError passes with no error found`() {
        val fileName = "SinglePublicClassJavaSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val error: Error? = JavaSampleSource.orThrow(file)
            .contentError()
        assertNull(error)
    }

    @Test
    fun `contentError fails with multiple classes found`() {
        val fileName = "MultipleClassesJavaSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val source: JavaSampleSource = JavaSampleSource.orThrow(file)
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("Multiple classes found in $source.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val fileName = "NoPublicClassJavaSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource not found.")
        val source: JavaSampleSource = JavaSampleSource.orThrow(file)
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("No public class found in $source.")
        assertEquals(expected, actual = error)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.java")
        val actual: String = JavaSampleSource.orThrow(file)
            .toString()
        assertEquals(expected = "'$file' Java sample source", actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with 'Sample' suffix in Java file's name`() {
        val file = File("Sample.java")
        val source: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNotNull(source)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `orNull fails with file having another extension than 'java'`() {
        val file = File("Sample.kt")
        val source: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(source)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.java")
        val source: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(source)
    }

    // ------------------------ Companion.orThrow(File) ------------------------

    @Test
    fun `orThrow passes with 'Sample' suffix in Java file's name`() {
        val file = File("Sample.java")
        val source: JavaSampleSource = JavaSampleSource.orThrow(file)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `orThrow fails with file having another extension than 'java'`() {
        val file = File("Sample.kt")
        val exception: IllegalArgumentException = assertFailsWith {
            JavaSampleSource.orThrow(file)
        }
        val expected = "'$file' file extension must be 'java'."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.java")
        val exception: IllegalArgumentException = assertFailsWith {
            JavaSampleSource.orThrow(file)
        }
        val expected = "'$file' file name must be suffixed by 'Sample'."
        assertEquals(expected, actual = exception.message)
    }
}
