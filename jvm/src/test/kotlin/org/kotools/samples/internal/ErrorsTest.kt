package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorsTest {
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
