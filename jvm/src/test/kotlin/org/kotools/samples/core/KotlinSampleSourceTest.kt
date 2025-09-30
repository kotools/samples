package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinSampleSourceTest {
    // ----------------------------- isValid(File) -----------------------------

    @Test
    fun `isValid passes with sample suffix and kt extension in file name`() {
        val file = File("Sample.kt")
        val actual: Boolean = KotlinSampleSource.isValid(file)
        assertTrue(actual)
    }

    @Test
    fun `isValid fails without sample suffix in file name`() {
        val file = File("Test.kt")
        val actual: Boolean = KotlinSampleSource.isValid(file)
        assertFalse(actual)
    }

    @Test
    fun `isValid fails without kt extension in file name`() {
        val file = File("Sample.java")
        val actual: Boolean = KotlinSampleSource.isValid(file)
        assertFalse(actual)
    }
}
