package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class KotlinSampleSourceTest {
    // ----------------------------- equals(Any?) ------------------------------

    @Test
    fun `equals passes with KotlinSampleSource corresponding to same file`() {
        val file = File("Sample.kt")
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val other: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val actual: Boolean = sampleSource == other
        assertTrue(actual)
    }

    @Test
    fun `equals fails with another type than KotlinSampleSource`() {
        val file = File("Sample.kt")
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orThrow(file)
        val actual: Boolean = sampleSource.equals(other = file)
        assertFalse(actual)
    }

    @Test
    fun `equals fails with KotlinSampleSource corresponding to another file`() {
        val file1 = File("FirstSample.kt")
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orThrow(file1)
        val file2 = File("SecondSample.kt")
        val other: KotlinSampleSource = KotlinSampleSource.orThrow(file2)
        val actual: Boolean = sampleSource == other
        assertFalse(actual)
    }

    // ------------------------------ hashCode() -------------------------------

    @Test
    fun `hashCode returns hash code value of corresponding file`() {
        val file = File("Sample.kt")
        val actual: Int = KotlinSampleSource.orThrow(file)
            .hashCode()
        val expected: Int = file.hashCode()
        assertEquals(expected, actual)
    }

    // ----------------------- contentExceptionOrNull() ------------------------

    @Test
    fun `contentExceptionOrNull passes with no content exception`() {
        val fileName = "SinglePublicClassSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = KotlinSampleSource.orThrow(file)
            .contentExceptionOrNull()
        assertNull(actual)
    }

    @Test
    fun `contentExceptionOrNull fails with multiple classes`() {
        val fileName = "MultipleClassesSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = KotlinSampleSource.orThrow(file)
            .contentExceptionOrNull()
        val expected: ExceptionMessage =
            ExceptionMessage.multipleClassesFoundIn(file)
        assertEquals(expected, actual)
    }

    @Test
    fun `contentExceptionOrNull fails with no public class`() {
        val fileName = "NoPublicClassSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = KotlinSampleSource.orThrow(file)
            .contentExceptionOrNull()
        val expected: ExceptionMessage =
            ExceptionMessage.noPublicClassFoundIn(file)
        assertEquals(expected, actual)
    }

    @Test
    fun `contentExceptionOrNull fails with single-expression function`() {
        val fileName = "SingleExpressionFunctionSample.kt"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = KotlinSampleSource.orThrow(file)
            .contentExceptionOrNull()
        val expected: ExceptionMessage =
            ExceptionMessage.singleExpressionKotlinFunctionFoundIn(file)
        assertEquals(expected, actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.kt")
        val actual: String = KotlinSampleSource.orThrow(file)
            .toString()
        val expected = "'${file.name}' Kotlin sample source"
        assertEquals(expected, actual)
    }

    // ------------------------------- toFile() --------------------------------

    @Test
    fun `toFile returns original file`() {
        val file = File("Sample.kt")
        val actual: File = KotlinSampleSource.orThrow(file)
            .toFile()
        assertEquals(expected = file, actual)
    }

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
