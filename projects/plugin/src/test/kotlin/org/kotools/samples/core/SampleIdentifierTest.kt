package org.kotools.samples.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SampleIdentifierTest {
    // ------------------------------- Creations -------------------------------

    @Test
    fun `from passes with text containing letters separated by dot`() {
        // Given
        val text = "IntSample.addition"

        // When
        val actual: SampleIdentifier = SampleIdentifier.from(text)

        // Then
        assertEquals(expected = text, "$actual")
    }

    @Test
    fun `from fails with text containing illegal characters`() {
        // Given
        val text = "IntSample addition"

        // When & Then
        val exception: IllegalArgumentException = assertFailsWith {
            SampleIdentifier.from(text)
        }
        val expected = "Sample identifier must contain letters separated by " +
                "dot (was: $text)."
        assertEquals(expected, exception.message)
    }

    // ------------------------------ Conversions ------------------------------

    @Test
    fun `toSamplePath passes`() {
        // Given
        val text = "IntSample.addition"
        val identifier: SampleIdentifier = SampleIdentifier.from(text)

        // When
        val actual: SamplePath = identifier.toSamplePath()

        // Then
        val path: String = text.replace(oldChar = '.', newChar = '/') + ".md"
        val expected: SamplePath = SamplePath.from(path)
        assertEquals(expected, actual)
    }
}
