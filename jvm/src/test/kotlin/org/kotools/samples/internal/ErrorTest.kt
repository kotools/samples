package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorTest {
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

    // -------------- File.singleExpressionKotlinFunctionFound() ---------------

    @Test
    fun `singleExpressionKotlinFunctionFound passes on File`() {
        val file = File("Sample.kt")
        val actual: String? = file.singleExpressionKotlinFunctionFound()
            .message
        val expected = "Single-expression Kotlin function found in '$file'."
        assertEquals(expected, actual)
    }

    // ------------------------ String.sampleNotFound() ------------------------

    @Test
    fun `sampleNotFound passes on sample identifier`() {
        val sampleIdentifier = "KotlinSample.test"
        val actual: String = sampleIdentifier.sampleNotFound()
        val expected = "'$sampleIdentifier' sample not found."
        assertEquals(expected, actual)
    }

    @Test
    fun `sampleNotFound fails on another String than sample identifier`() {
        val text = "KotlinSample-test"
        val exception: IllegalArgumentException =
            assertFailsWith(block = text::sampleNotFound)
        val actual: String? = exception.message
        val expected = "String is not a sample identifier (input: '$text')."
        assertEquals(expected, actual)
    }
}
