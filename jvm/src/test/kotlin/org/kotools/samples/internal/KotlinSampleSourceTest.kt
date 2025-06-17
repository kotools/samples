package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KotlinSampleSourceTest {
    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Kotlin file having name suffixed by 'Sample'`() {
        val file = File("Sample.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull fails with file's extension other than 'kt'`() {
        val file = File("Sample.java")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }

    // ------------------------ Companion.orThrow(File) ------------------------

    @Test
    fun `orThrow passes with Kotlin file having name suffixed by 'Sample'`() {
        val file = File("Sample.kt")
        KotlinSampleSource.orThrow(file)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.kt")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "'${file.nameWithoutExtension}' must ends with 'Sample'."
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow fails with file's extension other than 'kt'`() {
        val file = File("Sample.java")
        val exception: IllegalArgumentException =
            assertFailsWith { KotlinSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "'${file.extension}' must be 'kt'."
        assertEquals(expected, actual)
    }
}
