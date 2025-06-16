package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class SampleSourceFileTest {
    // ----------------------- checkSinglePublicClass() ------------------------

    @Test
    fun `checkSinglePublicClass passes with single public class in Java file`() {
        val name = "SinglePublicClassSample.java"
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        file.checkSinglePublicClass()
    }

    @Test
    fun `checkSinglePublicClass passes with single public class in Kotlin file`() {
        val name = "SinglePublicClassSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        file.checkSinglePublicClass()
    }

    @Test
    fun `checkSinglePublicClass fails with multiple classes in Java file`() {
        val name = "MultipleClassesSample.java"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected: String = ExceptionMessage.multipleClassesFoundIn(resource)
            .toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with multiple classes in Kotlin file`() {
        val name = "MultipleClassesSample.kt"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected: String = ExceptionMessage.multipleClassesFoundIn(resource)
            .toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with no public class in Java file`() {
        val name = "NoPublicClassSample.java"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected: String = ExceptionMessage.noPublicClassFoundIn(resource)
            .toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with no public class in Kotlin file`() {
        val name = "NoPublicClassSample.kt"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected: String = ExceptionMessage.noPublicClassFoundIn(resource)
            .toString()
        assertEquals(expected, actual)
    }

    // ---------------- checkNoSingleExpressionKotlinFunction() ----------------

    @Test
    fun `checkNoSingleExpressionKotlinFunction passes with Java file`() {
        val fileName = "SinglePublicClassSample.java"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        file.checkNoSingleExpressionKotlinFunction()
    }

    @Test
    fun `checkNoSingleExpressionKotlinFunction passes with no single-expression function in Kotlin file`() {
        val fileName = "SinglePublicClassSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        file.checkNoSingleExpressionKotlinFunction()
    }

    @Test
    fun `checkNoSingleExpressionKotlinFunction fails with single-expression function in Kotlin file`() {
        val fileName = "SingleExpressionFunctionSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        val exception: IllegalStateException =
            assertFailsWith(block = file::checkNoSingleExpressionKotlinFunction)
        val actual: String? = exception.message
        val expected = "Single-expression function found in '$file'."
        assertEquals(expected, actual)
    }

    // ------------------------------- samples() -------------------------------

    @Test
    fun `samples passes with package declaration in Java file`() {
        val name = "SinglePublicClassWithPackageSample.java"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val sample: Sample = sampleSource.samples()
            .toList()
            .first()
        assertEquals(
            expected = "sample.SinglePublicClassWithPackageSample.isPositive",
            actual = sample.identifier
        )
        assertEquals(
            expected = """
                |final int number = new Random()
                |        .nextInt(1, Integer.MAX_VALUE);
                |Assertions.assertTrue(number > 0);
            """.trimMargin(),
            actual = sample.body
        )
        assertEquals(expected = Java(), actual = sample.language)
    }

    @Test
    fun `samples passes without package declaration in Java file`() {
        val name = "SinglePublicClassSample.java"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val sample: Sample = sampleSource.samples()
            .toList()
            .first()
        assertEquals(
            expected = "SinglePublicClassSample.isPositive",
            actual = sample.identifier
        )
        assertEquals(
            expected = """
                |final int number = new Random()
                |        .nextInt(1, Integer.MAX_VALUE);
                |Assertions.assertTrue(number > 0);
            """.trimMargin(),
            actual = sample.body
        )
        assertEquals(expected = Java(), actual = sample.language)
    }

    @Test
    fun `samples passes with package declaration in Kotlin file`() {
        val name = "SinglePublicClassWithPackageSample.kt"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val sample: Sample = sampleSource.samples()
            .toList()
            .first()
        assertEquals(
            expected = "sample.SinglePublicClassWithPackageSample.isPositive",
            actual = sample.identifier
        )
        assertEquals(
            expected = """
                |val number: Int = (1..Int.MAX_VALUE)
                |    .random()
                |assertTrue(number > 0)
            """.trimMargin(),
            actual = sample.body
        )
        assertEquals(expected = Kotlin(), actual = sample.language)
    }

    @Test
    fun `samples passes without package declaration in Kotlin file`() {
        val name = "SinglePublicClassSample.kt"
        val resource: File = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' resource file not found.")
        val sampleSource: SampleSourceFile = SampleSourceFile.orNull(resource)
            ?: fail("'$name' sample source file not found.")
        val sample: Sample = sampleSource.samples()
            .toList()
            .first()
        assertEquals(
            expected = "SinglePublicClassSample.isPositive",
            actual = sample.identifier
        )
        assertEquals(
            expected = """
                |val number: Int = (1..Int.MAX_VALUE)
                |    .random()
                |assertTrue(number > 0)
            """.trimMargin(),
            actual = sample.body
        )
        assertEquals(expected = Kotlin(), actual = sample.language)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString behaves like File`() {
        val file = File("test/java/HelloSample.java")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("Invalid sample source file ($file).")
        val actual: String = sampleSourceFile.toString()
        val expected: String = file.toString()
        assertEquals(expected, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Java file`() {
        val file = File("HelloSample.java")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull passes with Kotlin file`() {
        val file = File("HelloSample.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails with file name not suffixed by 'Sample'`() {
        val file = File("Unsupported.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull fails with unsupported file`() {
        val file = File("UnsupportedSample.txt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }
}
