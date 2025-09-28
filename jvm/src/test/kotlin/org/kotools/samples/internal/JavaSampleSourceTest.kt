package org.kotools.samples.internal

import java.io.File
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.fail

class JavaSampleSourceTest {
    // ---------------------------- contentError() -----------------------------

    @Test
    fun `contentError passes without error`() {
        val error: Error? = JavaSampleSource
            .fromResources("SinglePublicClassJavaSample.java")
            .contentError()
        assertNull(error)
    }

    @Test
    fun `contentError fails with multiple classes found`() {
        val source: JavaSampleSource =
            JavaSampleSource.fromResources("MultipleClassesJavaSample.java")
        val actual: Error? = source.contentError()
        val expected: Error = "Multiple classes found in ${source}.".toError()
        assertEquals(expected, actual)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val source: JavaSampleSource = JavaSampleSource
            .fromResources("NoPublicClassJavaSample.java")
        val actual: Error? = source.contentError()
        val expected: Error = "No public class found in ${source}.".toError()
        assertEquals(expected, actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
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
        assertEquals(expected = file, actual = source?.file)
    }

    @Test
    fun `orNull fails with file having another extension than 'java'`() {
        val file = File("Sample.txt")
        val source: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(source)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in file's name`() {
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
        val file = File("Sample.txt")
        val exception: IllegalArgumentException = assertFailsWith {
            JavaSampleSource.orThrow(file)
        }
        val expected =
            "Java sample source must have 'java' file extension (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in file's name`() {
        val file = File("Test.java")
        val exception: IllegalArgumentException = assertFailsWith {
            JavaSampleSource.orThrow(file)
        }
        val expected = "Java sample source must have 'Sample' suffix in its " +
                "file name (input: $file)."
        assertEquals(expected, actual = exception.message)
    }
}

private fun JavaSampleSource.Companion.fromResources(
    path: String
): JavaSampleSource {
    val uri: URI = this::class.java.getResource("/$path")
        ?.toURI()
        ?: fail("$path resource file not found.")
    val file = File(uri)
    return this.orThrow(file)
}
