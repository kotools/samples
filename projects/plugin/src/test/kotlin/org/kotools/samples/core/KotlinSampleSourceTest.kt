package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class KotlinSampleSourceTest {
    // ------------------------------- Creations -------------------------------

    @Test
    fun `fromFileOrNull passes with kt file extension`() {
        // Given
        val file = File("Sample.kt")

        // When
        val actual: KotlinSampleSource? =
            KotlinSampleSource.fromFileOrNull(file)

        // Then
        assertNotNull(actual)
    }

    @Test
    fun `fromFileOrNull fails with another file extension than kt`() {
        // Given
        val file = File("Sample.java")

        // When
        val actual: KotlinSampleSource? =
            KotlinSampleSource.fromFileOrNull(file)

        // Then
        assertNull(actual)
    }

    // ---------------------------- File operations ----------------------------

    @Test
    fun `samples passes without top-level functions`() {
        // Given
        val file: File = resourceFile("ValidSamples.kt")
        val source: KotlinSampleSource? =
            KotlinSampleSource.fromFileOrNull(file)
        checkNotNull(source)

        // When
        val samples: Set<KotlinSample> = source.samples()

        // Then
        assertEquals(expected = 4, samples.size, "4 Kotlin samples expected.")

        val actualIntAddition: String = samples
            .first { it.identifier == "test.IntSample.addition" }
            .content
        val expectedIntAddition: String = """
            val x = 1
            val y = 2
            check(x + y == 3)
        """.trimIndent()
        assertEquals(expectedIntAddition, actualIntAddition)

        val actualIntSubtraction: String = samples
            .first { it.identifier == "test.IntSample.subtraction" }
            .content
        val expectedIntSubtraction = "check(2 - 1 == 1)"
        assertEquals(expectedIntSubtraction, actualIntSubtraction)

        val actualLongAddition: String = samples
            .first { it.identifier == "test.LongSample.addition" }
            .content
        val expectedLongAddition: String = """
            val x = 1L
            val y = 2L
            check(1L + 2L == 3L)
        """.trimIndent()
        assertEquals(expectedLongAddition, actualLongAddition)

        val actualLongSubtraction: String = samples
            .first { it.identifier == "test.LongSample.subtraction" }
            .content
        val expectedLongSubtraction = "check(2L - 1L == 1L)"
        assertEquals(expectedLongSubtraction, actualLongSubtraction)
    }

    @Test
    fun `samples fails with top-level function`() {
        // Given
        val file: File = resourceFile("TopLevelFunction.kt")
        val source: KotlinSampleSource? =
            KotlinSampleSource.fromFileOrNull(file)
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
        val source: KotlinSampleSource? =
            KotlinSampleSource.fromFileOrNull(file)
        checkNotNull(source)

        // When
        val actual: String = source.toString()

        // Then
        val expected = "'$file' Kotlin sample source"
        assertEquals(expected, actual)
    }
}
