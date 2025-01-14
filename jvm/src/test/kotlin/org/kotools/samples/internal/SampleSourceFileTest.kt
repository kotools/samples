package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class SampleSourceFileTest {
    // ----------------------- File content's operations -----------------------

    @Test
    fun `checkSingleClass should pass with a single class in it`() {
        val name = "/sample/kotlin/Valid.kt"
        this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orNull)
            ?.checkSingleClass()
            ?: fail("'$name' sample source file not found.")
    }

    @Test
    fun `checkSingleClass should fail without a class`() {
        val name = "/sample/kotlin/NoClass.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected =
            "The following file should have a single class: ${file.path}."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSingleClass should fail with multiple classes in it`() {
        val name = "/sample/kotlin/MultipleClasses.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orNull(file)
            ?: fail("'$name' sample source file not found.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected =
            "The following file should have a single class: ${file.path}."
        assertEquals(expected, actual)
    }

    // ------------------------------ Conversions ------------------------------

    @Test
    fun `toString should return an expressive string representation`() {
        val name = "/sample/kotlin/Valid.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val actual: String = SampleSourceFile.orNull(file)
            ?.toString()
            ?: fail("'$name' sample source file not found.")
        val expected = "Sample source file at '${file.path}'."
        assertEquals(expected, actual)
    }
}

class SampleSourceFileCompanionTest {
    @Test
    fun `orNull should pass with Java file in sample source set`(): Unit =
        this.orNullShouldPassWith("sample/java/Hello.java")

    @Test
    fun `orNull should pass with Kotlin file in sample source set`(): Unit =
        this.orNullShouldPassWith("sample/kotlin/Hello.kt")

    @Test
    fun `orNull should pass with Java file in test source set`(): Unit =
        this.orNullShouldPassWith("test/java/HelloSample.java")

    @Test
    fun `orNull should pass with Kotlin file in test source set`(): Unit =
        this.orNullShouldPassWith("test/kotlin/HelloSample.kt")

    private fun orNullShouldPassWith(path: String) {
        val file = File(path)
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file outside of sample or test source sets`():
            Unit = this.orNullShouldFailWith("Hello.kt")

    @Test
    fun `orNull should fail with unsupported file in sample source set`():
            Unit = this.orNullShouldFailWith("sample/Unsupported.txt")

    @Test
    fun `orNull should fail with invalid file suffix in test source set`():
            Unit = this.orNullShouldFailWith("test/kotlin/Unsupported.kt")

    @Test
    fun `orNull should fail with unsupported file in test source set`(): Unit =
        this.orNullShouldFailWith("test/UnsupportedSample.txt")

    private fun orNullShouldFailWith(path: String) {
        val file = File(path)
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }
}
