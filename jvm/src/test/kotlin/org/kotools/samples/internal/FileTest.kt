package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
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

    // ----------------------------- File.isJava() -----------------------------

    @Test
    fun `isJava passes on File with 'java' extension`() {
        val actual: Boolean = File("Sample.java")
            .isJava()
        assertTrue(actual)
    }

    @Test
    fun `isJava fails on File having another extension than 'java'`() {
        val actual: Boolean = File("Sample.kt")
            .isJava()
        assertFalse(actual)
    }

    // ---------------------- File.multipleClassesFound() ----------------------

    @Test
    fun `multipleClassesFound passes on File`() {
        val file = File("Sample.kt")
        val actual: String? = file.multipleClassesFound()
            .message
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    // ----------------------- File.noPublicClassFound() -----------------------

    @Test
    fun `noPublicClassFound passes on File`() {
        val file = File("Sample.kt")
        val actual: String? = file.noPublicClassFound()
            .message
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual)
    }
}
