package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class KotlinFileTest {
    // ------------------------------- Creations -------------------------------

    @Test
    fun `fromOrNull passes with kt file extension`() {
        // Given
        val file = File("Sample.kt")

        // When
        val actual: KotlinFile? = KotlinFile.fromOrNull(file)

        // Then
        assertNotNull(actual)
    }

    @Test
    fun `fromOrNull fails with another file extension than kt`() {
        // Given
        val file = File("Sample.java")

        // When
        val actual: KotlinFile? = KotlinFile.fromOrNull(file)

        // Then
        assertNull(actual)
    }

    // ----------------------------- File reading ------------------------------

    @Test
    fun `samples passes with empty file`() {
        // Given
        val file: File = resourceFile("Empty.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val samples: Set<KotlinSample> = source.samples()

        // Then
        assertTrue(samples.isEmpty())
    }

    @Test
    fun `samples passes with classes and member functions`() {
        // Given
        val file: File = resourceFile("MemberFunctions.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val actual: Set<KotlinSample> = source.samples()

        // Then
        val expected: Set<KotlinSample> = setOf(
            KotlinSample.from(
                SampleIdentifier.from("test.IntSample.addition"),
                content = """
                    val x = 1
                    val y = 2
                    check(x + y == 3)
                """.trimIndent()
            ),
            KotlinSample.from(
                SampleIdentifier.from("test.IntSample.subtraction"),
                content = "check(2 - 1 == 1)"
            ),
            KotlinSample.from(
                SampleIdentifier.from("test.LongSample.addition"),
                content = """
                    val x = 1L
                    val y = 2L
                    check(x + y == 3L)
                """.trimIndent()
            ),
            KotlinSample.from(
                SampleIdentifier.from("test.LongSample.subtraction"),
                content = "check(2L - 1L == 1L)"
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `samples fails with top-level function`() {
        // Given
        val file: File = resourceFile("TopLevelFunction.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val exception: FileSystemException =
            assertFailsWith(block = source::samples)

        // Then
        assertEquals(expected = file, actual = exception.file)
        assertNull(exception.other)
        assertEquals(
            expected = "Top-level function found in Kotlin sample source.",
            actual = exception.reason
        )
    }

    @Test
    fun `sampleIdentifiers passes with sample references`() {
        // Given
        val file: File = this.resourceFile("SampleReferences.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val actual: Set<SampleIdentifier> = source.sampleIdentifiers()

        // Then
        val expected: Set<SampleIdentifier> = setOf(
            SampleIdentifier.from("test.IntSample.addition"),
            SampleIdentifier.from("test.LongSample.addition"),
            SampleIdentifier.from("test.IntSample.subtraction"),
            SampleIdentifier.from("test.LongSample.subtraction")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `sampleIdentifiers passes without sample references`() {
        // Given
        val file: File = this.resourceFile("Empty.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val identifiers: Set<SampleIdentifier> = source.sampleIdentifiers()

        // Then
        assertTrue(identifiers.isEmpty())
    }

    private fun resourceFile(name: String): File = this::class.java
        .getResource("/$name")
        ?.toURI()
        ?.let(::File)
        ?: fail("Resource file not found (was: $name).")

    // ------------------------------ Conversions ------------------------------

    @Test
    fun `toString passes`() {
        // Given
        val file = File("Sample.kt")
        val source: KotlinFile? = KotlinFile.fromOrNull(file)
        checkNotNull(source)

        // When
        val actual: String = source.toString()

        // Then
        val expected = "'$file' Kotlin sample source"
        assertEquals(expected, actual)
    }
}
