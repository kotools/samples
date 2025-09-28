package org.kotools.samples.internal

import java.io.File
import java.net.URI
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

    // ---------------------------- contentError() -----------------------------

    @Test
    fun `contentError passes without error`() {
        val error: Error? = KotlinSampleSource
            .fromResources("SinglePublicClassKotlinSample.kt")
            .contentError()
        assertNull(error)
    }

    @Test
    fun `contentError fails with multiple classes found`() {
        val source: KotlinSampleSource =
            KotlinSampleSource.fromResources("MultipleClassesKotlinSample.kt")
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("Multiple classes found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val source: KotlinSampleSource =
            KotlinSampleSource.fromResources("NoPublicClassKotlinSample.kt")
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("No public class found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with single-expression function found`() {
        val source: KotlinSampleSource = KotlinSampleSource
            .fromResources("SingleExpressionFunctionKotlinSample.kt")
        val error: Error? = source.contentError()
        val expected: Error =
            Error.orThrow("Single-expression function found in ${source}.")
        assertEquals(expected, actual = error)
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
}

private fun KotlinSampleSource.Companion.fromResources(
    path: String
): KotlinSampleSource {
    val uri: URI = this::class.java.getResource("/$path")
        ?.toURI()
        ?: fail("$path resource file not found.")
    val file = File(uri)
    return file.toKotlinSampleSourceOrNull()
        ?: fail("$file is not a Kotlin sample source.")
}
