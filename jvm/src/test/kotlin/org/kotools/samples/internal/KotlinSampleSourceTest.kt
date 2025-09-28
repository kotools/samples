package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class KotlinSampleSourceTest {
    // ------------------- File.toKotlinSampleSourceOrNull() -------------------

    @Test
    fun toKotlinSampleSourceOrNullPassesOnFileNameEndingWithSampleKt() {
        val actual: KotlinSampleSource? = File("Sample.kt")
            .toKotlinSampleSourceOrNull()
        assertNotNull(actual)
    }

    @Test
    fun toKotlinSampleSourceOrNullFailsOnFileNameNotEndingWithSampleKt() {
        val actual: KotlinSampleSource? = File("Sample.java")
            .toKotlinSampleSourceOrNull()
        assertNull(actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource = file.toKotlinSampleSourceOrNull()
            ?: fail("$file is not a Kotlin sample source.")
        val actual: String = source.toString()
        val expected = "'$file' Kotlin sample source"
        assertEquals(expected, actual)
    }

    // ---------------------------- contentError() -----------------------------

    @Test
    fun `contentError passes without error`() {
        val error: Error? = "SinglePublicClassKotlinSample.kt"
            .resourceToKotlinSampleSource()
            .contentError()
        assertNull(error)
    }

    @Test
    fun `contentError fails with multiple classes found`() {
        val source: KotlinSampleSource =
            "MultipleClassesKotlinSample.kt".resourceToKotlinSampleSource()
        val error: Error? = source.contentError()
        val expected = Error("Multiple classes found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val source: KotlinSampleSource =
            "NoPublicClassKotlinSample.kt".resourceToKotlinSampleSource()
        val error: Error? = source.contentError()
        val expected = Error("No public class found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with single-expression function found`() {
        val source: KotlinSampleSource =
            "SingleExpressionFunctionKotlinSample.kt"
                .resourceToKotlinSampleSource()
        val error: Error? = source.contentError()
        val expected = Error("Single-expression function found in ${source}.")
        assertEquals(expected, actual = error)
    }
}

private fun String.resourceToKotlinSampleSource(): KotlinSampleSource =
    KotlinSampleSource::class.java.getResource("/$this")
        ?.toURI()
        ?.let(::File)
        ?.toKotlinSampleSourceOrNull()
        ?: fail("$this Kotlin sample source not found from resources.")
