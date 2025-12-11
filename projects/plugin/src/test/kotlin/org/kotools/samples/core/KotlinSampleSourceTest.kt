package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
