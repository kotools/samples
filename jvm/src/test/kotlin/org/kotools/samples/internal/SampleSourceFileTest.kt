package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class SampleSourceFileTest {
    // -------------------------- checkSingleClass() ---------------------------

    @Test
    fun `checkSingleClass should pass with a single class in it`() {
        val name = "/ValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        file.checkSingleClass()
    }

    @Test
    fun `checkSingleClass should fail without a class`() {
        val name = "/NoClassSample.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected = "No class found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSingleClass should fail with multiple classes in it`() {
        val name = "/MultipleClassesSample.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    // ----------------------- packageIdentifierOrNull() -----------------------

    @Test
    fun `packageIdentifierOrNull with package declaration in file`() {
        val fileName = "ValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        val actual: String? = file.packageIdentifierOrNull()
        val expected = "sample.kotlin"
        assertEquals(expected, actual)
    }

    @Test
    fun `packageIdentifierOrNull without package declaration in file`() {
        val fileName = "NoPackageValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        val actual: String? = file.packageIdentifierOrNull()
        assertNull(actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString should behave like File`() {
        val file = File("test/java/HelloSample.java")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("Invalid sample source file ($file).")
        val actual: String = sampleSourceFile.toString()
        val expected: String = file.toString()
        assertEquals(expected, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull should pass with Java file in test source set`() {
        val file = File("test/java/HelloSample.java")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should pass with Kotlin file in test source set`() {
        val file = File("test/kotlin/HelloSample.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file outside of test source set`() {
        val file = File("Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with invalid file suffix in test source set`() {
        val file = File("test/kotlin/Unsupported.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with unsupported file in test source set`() {
        val file = File("test/UnsupportedSample.txt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }
}
