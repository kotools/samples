package org.kotools.samples.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinSampleTest {
    // ------------------------------- Creations -------------------------------

    @Test
    fun `from passes with non-blank content`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()

        // When & Then
        KotlinSample.from(identifier, content)
    }

    @Test
    fun `from fails with blank content`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content = " "

        // When & Then
        val exception: IllegalArgumentException = assertFailsWith {
            KotlinSample.from(identifier, content)
        }
        val actual: String? = exception.message
        val expected = "Kotlin sample's content can't be blank."
        assertEquals(expected, actual)
    }

    // -------------------- Structural equality operations ---------------------

    @Test
    fun `equals passes with Sample having same identifier`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)
        val other: KotlinSample =
            KotlinSample.from(identifier, "$content\nprintln(1)")

        // When
        val actual: Boolean = sample == other

        // Then
        assertTrue(actual)
    }

    @Test
    fun `equals fails with another type than Sample`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)

        // When
        val actual: Boolean = sample.equals(other = identifier)

        // Then
        assertFalse(actual)
    }

    @Test
    fun `equals fails with Sample having another identifier`() {
        // Given
        val identifier1: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier1, content)
        val identifier2: SampleIdentifier =
            SampleIdentifier.from("${identifier1}V2")
        val other: KotlinSample = KotlinSample.from(identifier2, content)

        // When
        val actual: Boolean = sample == other

        // Then
        assertFalse(actual)
    }

    @Test
    fun `hashCode returns hash code value of identifier`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)

        // When
        val actual: Int = sample.hashCode()

        // Then
        val expected: Int = identifier.hashCode()
        assertEquals(expected, actual)
    }

    // -------------------------- Markdown operations --------------------------

    @Test
    fun `markdownFilePath passes`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)

        // When
        val actual: String = sample.markdownFilePath()

        // Then
        val expected: String = identifier.toSamplePath()
            .toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `markdownFileContent passes`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)

        // When
        val actual: String = sample.markdownFileContent()

        // Then
        val expected: String = """
            |```kotlin
            |$content
            |```
        """.trimMargin()
        assertEquals(expected, actual)
    }

    // ------------------------------ Conversions ------------------------------

    @Test
    fun `toString passes`() {
        // Given
        val identifier: SampleIdentifier =
            SampleIdentifier.from("IntSample.unaryMinus")
        val content: String = """
            val x = 1
            val y = -1
            check(-x == y)
        """.trimIndent()
        val sample: KotlinSample = KotlinSample.from(identifier, content)

        // When
        val actual: String = sample.toString()

        // Then
        val expected = "'$identifier' Kotlin sample"
        assertEquals(expected, actual)
    }
}
