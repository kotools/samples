package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JavaSampleSourceTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("Sample.java")
        val actual: String = JavaSampleSource.orThrow(file)
            .toString()
        assertEquals(expected = "'$file' Java sample source", actual)
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
