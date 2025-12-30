package org.kotools.samples.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SamplePathTest {
    // ------------------------------- Creations -------------------------------

    @Test
    fun `from passes with text suffixed by Markdown file extension`() {
        // Given
        val text = "IntSample/addition.md"

        // When
        val actual: SamplePath = SamplePath.from(text)

        // Then
        assertEquals(expected = text, "$actual")
    }

    @Test
    fun `from fails with text not not suffixed by Markdown file extension`() {
        // Given
        val text = "IntSample/addition.txt"

        // When & Then
        val exception: IllegalArgumentException = assertFailsWith {
            SamplePath.from(text)
        }
        val expected =
            "Sample path must end with 'md' file extension (was: $text)."
        assertEquals(expected, exception.message)
    }
}
