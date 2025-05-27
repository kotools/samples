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
    fun `checkSingleClass passes with a single class in it`() {
        val name = "/ValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        file.checkSingleClass()
    }

    @Test
    fun `checkSingleClass fails without a class`() {
        val name = "/NoPublicClassSample.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSingleClass fails with multiple classes in it`() {
        val name = "/MultiplePublicClassesSample.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected = "Multiple public classes found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSingleClass fails with nested public classes in it`() {
        val name = "/NestedPublicClassSample.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected = "Multiple public classes found in '$file'."
        assertEquals(expected, actual)
    }

    // ----------------------- packageIdentifierOrNull() -----------------------

    @Test
    fun `packageIdentifierOrNull passes with package declaration in file`() {
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
    fun `packageIdentifierOrNull fails without package declaration in file`() {
        val fileName = "NoPackageValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        val actual: String? = file.packageIdentifierOrNull()
        assertNull(actual)
    }

    // ------------------------------ className() ------------------------------

    @Test
    fun `className passes`() {
        val fileName = "ValidSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$fileName")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$fileName' sample source file not found.")
        val actual: String = file.className()
        val expected = "ValidSample"
        assertEquals(expected, actual)
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
