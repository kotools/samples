package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileTest {
    // ---------------------------- File.isSample() ----------------------------

    @Test
    fun `isSample passes on File named with 'Sample' suffix`() {
        val actual: Boolean = File("Sample.kt")
            .isSample()
        assertTrue(actual)
    }

    @Test
    fun `isSample fails on File named without 'Sample' suffix`() {
        val actual: Boolean = File("Test.kt")
            .isSample()
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
    fun `isKotlin fails on File having another extension than 'kt'`() {
        val actual: Boolean = File("Sample.java")
            .isKotlin()
        assertFalse(actual)
    }
}
