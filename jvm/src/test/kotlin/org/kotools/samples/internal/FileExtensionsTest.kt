package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileExtensionsTest {
    // ------------------------- File.isSampleSource() -------------------------

    @Test
    fun `isSampleSource passes on File with 'Sample' suffix in its name`() {
        val actual: Boolean = File("Sample.kt")
            .isSampleSource()
        assertTrue(actual)
    }

    @Test
    fun `isSampleSource fails on File without 'Sample' suffix in its name`() {
        val actual: Boolean = File("Test.kt")
            .isSampleSource()
        assertFalse(actual)
    }

    // ---------------------------- File.isKotlin() ----------------------------

    @Test
    fun `isKotlin passes on File with 'kt' extension`() {
        val actual: Boolean = File("Sample.kt")
            .isKotlin()
        assertTrue(actual)
    }

    @Test
    fun `isKotlin fails on File with another extension than 'kt'`() {
        val actual: Boolean = File("Sample.java")
            .isKotlin()
        assertFalse(actual)
    }
}
