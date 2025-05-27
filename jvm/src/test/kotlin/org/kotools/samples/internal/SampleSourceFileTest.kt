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
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = file::checkSinglePublicClass
        ).message
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with multiple classes in Kotlin file`() {
        val name = "MultipleClassesSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = file::checkSinglePublicClass
        ).message
        val expected = "Multiple classes found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with no public class in Java file`() {
        val name = "NoPublicClassSample.java"
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = file::checkSinglePublicClass
        ).message
        val expected = "No public class found in '$file'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with no public class in Kotlin file`() {
        val name = "NoPublicClassSample.kt"
        val file: SampleSourceFile = this::class.java.getResource("/$name")
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = file::checkSinglePublicClass
        ).message
        val expected = "No public class found in '$file'."
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
