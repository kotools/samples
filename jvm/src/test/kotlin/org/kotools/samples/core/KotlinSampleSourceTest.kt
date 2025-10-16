package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KotlinSampleSourceTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.kt")
        val actual: String = checkNotNull(KotlinSampleSource of file)
            .toString()
        val expected = "'${file.name}' Kotlin sample source"
        assertEquals(expected, actual)
    }

    // -------------------------- Companion.of(File) ---------------------------

    @Test
    fun `of passes with sample suffix and kt extension in file name`() {
        val file = File("Sample.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource of file
        assertNotNull(actual)
    }

    @Test
    fun `of fails without sample suffix in file name`() {
        val file = File("Test.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource of file
        assertNull(actual)
    }

    @Test
    fun `of fails without kt extension in file name`() {
        val file = File("Sample.java")
        val actual: KotlinSampleSource? = KotlinSampleSource of file
        assertNull(actual)
    }
}
