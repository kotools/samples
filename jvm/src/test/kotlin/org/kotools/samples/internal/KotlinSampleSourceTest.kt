package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KotlinSampleSourceTest {
    // --------------------------- constructor(File) ---------------------------

    @Test
    fun `constructor passes with 'Sample' suffix in Kotlin file's name`() {
        val file = File("Sample.kt")
        val source = KotlinSampleSource(file)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `constructor fails with file having another extension than 'kt'`() {
        val file = File("Sample.java")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource(file) }
        val expected =
            "Kotlin sample source must have 'kt' file extension (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `constructor fails without 'Sample' suffix in file's name`() {
        val file = File("Test.kt")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource(file) }
        val expected = "Kotlin sample source must have 'Sample' suffix in " +
                "its file name (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("Sample.kt")
        val actual: String = KotlinSampleSource(file)
            .toString()
        val expected = "'$file' Kotlin sample source"
        assertEquals(expected, actual)
    }
}
