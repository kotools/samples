package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JavaSampleSourceTest {
    // --------------------------- constructor(File) ---------------------------

    @Test
    fun `constructor passes with 'Sample' suffix in Java file's name`() {
        val file = File("Sample.java")
        val source = JavaSampleSource(file)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `constructor fails with file having another extension than 'java'`() {
        val file = File("Sample.txt")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource(file) }
        val expected =
            "Java sample source must have 'java' file extension (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `constructor fails without 'Sample' suffix in file's name`() {
        val file = File("Test.java")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource(file) }
        val expected = "Java sample source must have 'Sample' suffix in its " +
                "file name (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("Sample.java")
        val actual: String = JavaSampleSource(file)
            .toString()
        assertEquals(expected = "'$file' Java sample source", actual)
    }
}
