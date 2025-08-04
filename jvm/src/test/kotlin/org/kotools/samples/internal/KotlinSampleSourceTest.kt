package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KotlinSampleSourceTest {
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
