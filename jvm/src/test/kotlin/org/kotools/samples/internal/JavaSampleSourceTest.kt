package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JavaSampleSourceTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.java")
        val actual: String = JavaSampleSource.orThrow(file)
            .toString()
        val expected = "'${file.name}' Java sample source"
        assertEquals(expected, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Java file having name suffixed by 'Sample'`() {
        val file = File("Sample.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Java file's name`() {
        val file = File("Test.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun `orNull fails with file's extension other than 'java'`() {
        val file = File("Sample.kt")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }

    // ------------------------ Companion.orThrow(File) ------------------------

    @Test
    fun `orThrow passes with Java file having name suffixed by 'Sample'`() {
        val file = File("Sample.java")
        JavaSampleSource.orThrow(file)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in Java file's name`() {
        val file = File("Test.java")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "Java sample source file name must end with 'Sample' " +
                "(input: '${file.nameWithoutExtension}')."
        assertEquals(expected, actual)
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun `orThrow fails with file's extension other than 'java'`() {
        val file = File("Sample.kt")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "Java sample source file extension must be 'java' " +
                "(input: '${file.extension}')."
        assertEquals(expected, actual)
    }
}
