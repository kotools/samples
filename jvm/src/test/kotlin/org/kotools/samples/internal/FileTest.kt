package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTest {
    // ---------------------------- File.isSample() ----------------------------

    @Test
    fun `isSample passes with 'Sample' suffix in file's name`() {
        val actual: Boolean = File("KotlinSample.kt")
            .isSample()
        assertTrue(actual)
    }

    @Test
    fun `isSample fails without 'Sample' suffix in file's name`() {
        val actual: Boolean = File("Kotlin.kt")
            .isSample()
        assertFalse(actual)
    }

    // ---------------------------- File.isKotlin() ----------------------------

    @Test
    fun `isKotlin passes with file's extension being 'kt'`() {
        val actual: Boolean = File("Sample.kt")
            .isKotlin()
        assertTrue(actual)
    }

    @Test
    fun `isKotlin fails with another file's extension than 'kt'`() {
        val actual: Boolean = File("Sample.java")
            .isKotlin()
        assertFalse(actual)
    }
}
