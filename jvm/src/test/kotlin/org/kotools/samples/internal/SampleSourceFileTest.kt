package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class SampleSourceFileTest {
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
