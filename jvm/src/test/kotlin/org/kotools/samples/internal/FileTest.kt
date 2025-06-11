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
}
