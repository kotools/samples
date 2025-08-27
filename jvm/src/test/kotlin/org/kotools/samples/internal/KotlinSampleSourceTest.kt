package org.kotools.samples.internal

import java.io.File
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.fail

class KotlinSampleSourceTest {
    // --------------------------- constructor(File) ---------------------------

    @Test
    fun `constructor passes with 'Sample' suffix in Kotlin file's name`() {
        val file = File("Sample.kt")
        val source = KotlinSampleSource(file)
        assertEquals(expected = file, actual = source.file)
    }

    @Test
    fun `constructor fails with file having another extension than 'kt'`() {
        val file = File("Sample.java")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource(file) }
        val expected =
            "Kotlin sample source must have 'kt' file extension (input: $file)."
        assertEquals(expected, actual = exception.message)
    }

    @Test
    fun `constructor fails without 'Sample' suffix in file's name`() {
        val file = File("Test.kt")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource(file) }
        val expected = "Kotlin sample source must have 'Sample' suffix in " +
                "its file name (input: $file)."
        assertEquals(expected, actual = exception.message)
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
        val expected = Error("Multiple classes found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with no public class found`() {
        val source: KotlinSampleSource =
            KotlinSampleSource.fromResources("NoPublicClassKotlinSample.kt")
        val error: Error? = source.contentError()
        val expected = Error("No public class found in ${source}.")
        assertEquals(expected, actual = error)
    }

    @Test
    fun `contentError fails with single-expression function found`() {
        val source: KotlinSampleSource = KotlinSampleSource
            .fromResources("SingleExpressionFunctionKotlinSample.kt")
        val error: Error? = source.contentError()
        val expected = Error("Single-expression function found in ${source}.")
        assertEquals(expected, actual = error)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("Sample.kt")
        val actual: String = KotlinSampleSource(file)
            .toString()
        val expected = "'$file' Kotlin sample source"
        assertEquals(expected, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with 'Sample' suffix in Kotlin file's name`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertEquals(expected = file, actual = source?.file)
    }

    @Test
    fun `orNull fails with file having another extension than 'kt'`() {
        val file = File("Sample.java")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(source)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in file's name`() {
        val file = File("Test.kt")
        val source: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(source)
    }
}

private fun KotlinSampleSource.Companion.fromResources(
    path: String
): KotlinSampleSource {
    val uri: URI = this::class.java.getResource("/$path")
        ?.toURI()
        ?: fail("$path resource file not found.")
    val file = File(uri)
    return KotlinSampleSource(file)
}
