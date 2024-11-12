package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class SampleSourceFileTest {
    @Test
    fun `checkSingleClass should pass with a single class in it`() {
        val name = "/sample/kotlin/Valid.kt"
        this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?.let(SampleSourceFile.Companion::orThrow)
            ?.checkSingleClass()
            ?: fail("'$name' file not found.")
    }

    @Test
    fun `checkSingleClass should fail without a class`() {
        val name = "/sample/kotlin/NoClass.kt"
        val file: File = this::class.java.getResource(name)
            ?.toURI()
            ?.let(::File)
            ?: fail("'$name' file not found.")
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orThrow(file)
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
        val sampleSourceFile: SampleSourceFile = SampleSourceFile.orThrow(file)
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSourceFile::checkSingleClass
        ).message
        val expected =
            "The following file should have a single class: ${file.path}."
        assertEquals(expected, actual)
    }
}

class SampleSourceFileCompanionTest {
    @Test
    fun `orNull should pass with Java file in sample source set`() {
        val file = File("sample/java/Hello.java")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should pass with Kotlin file in sample source set`() {
        val file = File("sample/kotlin/Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file outside sample source set`() {
        val file = File("Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with unsupported file in sample source set`() {
        val file = File("sample/Unsupported.txt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orThrow should pass with Java file in sample source set`() {
        val file = File("sample/java/Hello.java")
        SampleSourceFile.orThrow(file)
    }

    @Test
    fun `orThrow should pass with Kotlin file in sample source set`() {
        val file = File("sample/kotlin/Hello.kt")
        SampleSourceFile.orThrow(file)
    }

    @Test
    fun `orThrow should fail with file outside sample source set`() {
        val file = File("Hello.kt")
        val expected = "'${file.name}' file should be in a sample source set."
        val actual: String? = assertFailsWith<IllegalArgumentException> {
            SampleSourceFile.orThrow(file)
        }.message
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow should fail with unsupported file in sample source set`() {
        val file = File("sample/Unsupported.txt")
        val expected = "'.${file.extension}' files are not supported."
        val actual: String? = assertFailsWith<IllegalArgumentException> {
            SampleSourceFile.orThrow(file)
        }.message
        assertEquals(expected, actual)
    }
}
