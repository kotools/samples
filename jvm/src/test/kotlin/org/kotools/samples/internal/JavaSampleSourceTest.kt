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

class JavaSampleSourceTest {
    // ----------------------------- equals(Any?) ------------------------------

    @Test
    fun `equals passes with JavaSampleSource corresponding to same file`() {
        val file = File("Sample.java")
        val sampleSource: JavaSampleSource = JavaSampleSource.orThrow(file)
        val other: JavaSampleSource = JavaSampleSource.orThrow(file)
        val actual: Boolean = sampleSource == other
        assertTrue(actual)
    }

    @Test
    fun `equals fails with another type than JavaSampleSource`() {
        val file = File("Sample.java")
        val sampleSource: JavaSampleSource = JavaSampleSource.orThrow(file)
        val actual: Boolean = sampleSource.equals(file)
        assertFalse(actual)
    }

    @Test
    fun `equals fails with JavaSampleSource corresponding to another file`() {
        val file1 = File("FirstSample.java")
        val sampleSource: JavaSampleSource = JavaSampleSource.orThrow(file1)
        val file2 = File("SecondSample.java")
        val other: JavaSampleSource = JavaSampleSource.orThrow(file2)
        val actual: Boolean = sampleSource == other
        assertFalse(actual)
    }

    // ------------------------------ hashCode() -------------------------------

    @Test
    fun `hashCode returns hash code value of corresponding file`() {
        val file = File("Sample.java")
        val actual: Int = JavaSampleSource.orThrow(file)
            .hashCode()
        val expected: Int = file.hashCode()
        assertEquals(expected, actual)
    }

    // ----------------------- contentExceptionOrNull() ------------------------

    @Test
    fun `contentExceptionOrNull passes with no content exception`() {
        val fileName = "SinglePublicClassSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = JavaSampleSource.orThrow(file)
            .contentExceptionOrNull()
        assertNull(actual)
    }

    @Test
    fun `contentExceptionOrNull fails with multiple classes`() {
        val fileName = "MultipleClassesSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = JavaSampleSource.orThrow(file)
            .contentExceptionOrNull()
        val expected: ExceptionMessage =
            ExceptionMessage.multipleClassesFoundIn(file)
        assertEquals(expected, actual)
    }

    @Test
    fun `contentExceptionOrNull fails with no public class`() {
        val fileName = "NoPublicClassSample.java"
        val file: File = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$fileName' resource file not found.")
        val actual: ExceptionMessage? = JavaSampleSource.orThrow(file)
            .contentExceptionOrNull()
        val expected: ExceptionMessage =
            ExceptionMessage.noPublicClassFoundIn(file)
        assertEquals(expected, actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns meaningful string representation`() {
        val file = File("Sample.java")
        val actual: String = JavaSampleSource.orThrow(file)
            .toString()
        val expected = "'${file.name}' Java sample source"
        assertEquals(expected, actual)
    }

    // ------------------------------- toFile() --------------------------------

    @Test
    fun `toFile returns original file`() {
        val file = File("Sample.java")
        val actual: File = JavaSampleSource.orThrow(file)
            .toFile()
        assertEquals(expected = file, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Java file having name suffixed by 'Sample'`() {
        val file = File("Sample.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Java file's name`() {
        val file = File("Test.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun `orNull fails with file's extension other than 'java'`() {
        val file = File("Sample.kt")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }

    // ------------------------ Companion.orThrow(File) ------------------------

    @Test
    fun `orThrow passes with Java file having name suffixed by 'Sample'`() {
        val file = File("Sample.java")
        JavaSampleSource.orThrow(file)
    }

    @Test
    fun `orThrow fails without 'Sample' suffix in Java file's name`() {
        val file = File("Test.java")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "Java sample source file name must end with 'Sample' " +
                "(input: '${file.nameWithoutExtension}')."
        assertEquals(expected, actual)
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun `orThrow fails with file's extension other than 'java'`() {
        val file = File("Sample.kt")
        val exception: IllegalArgumentException =
            assertFailsWith { JavaSampleSource.orThrow(file) }
        val actual: String? = exception.message
        val expected = "Java sample source file extension must be 'java' " +
                "(input: '${file.extension}')."
        assertEquals(expected, actual)
    }
}
