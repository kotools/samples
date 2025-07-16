package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringTest {
    // -------------------------- String.isPackage() ---------------------------

    @Test
    fun `isPackage passes on package declaration with semicolon`() {
        val actual: Boolean = "package samples;".isPackage()
        assertTrue(actual)
    }

    @Test
    fun `isPackage passes on package declaration without semicolon`() {
        val actual: Boolean = "package samples".isPackage()
        assertTrue(actual)
    }

    @Test
    fun `isPackage fails on String missing 'package' keyword`() {
        val actual: Boolean = "samples".isPackage()
        assertFalse(actual)
    }

    @Test
    fun `isPackage fails on String missing package identifier`() {
        val actual: Boolean = "package".isPackage()
        assertFalse(actual)
    }

    // ---------------------- String.packageIdentifier() -----------------------

    @Test
    fun `packageIdentifier passes on package declaration with semicolon`() {
        val expected = "samples"
        val actual: String = "package $expected;".packageIdentifier()
        assertEquals(expected, actual)
    }

    @Test
    fun `packageIdentifier passes on package declaration without semicolon`() {
        val expected = "samples"
        val actual: String = "package $expected".packageIdentifier()
        assertEquals(expected, actual)
    }

    @Test
    fun `packageIdentifier fails on another String than package declaration`() {
        val text = "package"
        val exception: IllegalArgumentException =
            assertFailsWith(block = text::packageIdentifier)
        val actual: String? = exception.message
        val expected = "String is not a package declaration (input: '$text')."
        assertEquals(expected, actual)
    }

    // --------------------------- String.isClass() ----------------------------

    @Test
    fun `isClass passes on String with 'class' keyword and class name`() {
        val actual: Boolean = "class A".isClass()
        assertTrue(actual)
    }

    @Test
    fun `isClass fails on String missing 'class' keyword`() {
        val actual: Boolean = "A".isClass()
        assertFalse(actual)
    }

    @Test
    fun `isClass fails on String missing class name`() {
        val actual: Boolean = "class".isClass()
        assertFalse(actual)
    }

    // -------------------------- String.className() ---------------------------

    @Test
    fun `className passes on class declaration`() {
        val expected = "Sample"
        val actual: String = "class $expected {".className()
        assertEquals(expected, actual)
    }

    @Test
    fun `className fails on another String than class declaration`() {
        val text = "class"
        val exception: IllegalArgumentException =
            assertFailsWith(block = text::className)
        val actual: String? = exception.message
        val expected = "String is not a class declaration (input: '$text')."
        assertEquals(expected, actual)
    }

    // --------------------- String.isKotlinPublicClass() ----------------------

    @Test
    fun `isKotlinPublicClass passes on Kotlin class with no visibility`() {
        val actual: Boolean = "class A".isKotlinPublicClass()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinPublicClass passes on Kotlin class with 'public' modifier`() {
        val actual: Boolean = "public class A".isKotlinPublicClass()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinPublicClass fails on String not being Kotlin class`() {
        val actual: Boolean = "val x = 1".isKotlinPublicClass()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinPublicClass fails on internal Kotlin class`() {
        val actual: Boolean = "internal class A".isKotlinPublicClass()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinPublicClass fails on private Kotlin class`() {
        val actual: Boolean = "private class A".isKotlinPublicClass()
        assertFalse(actual)
    }

    // ----------------------- String.isKotlinFunction() -----------------------

    @Test
    fun `isKotlinFunction passes on Kotlin function with return-type`() {
        val actual: Boolean = "fun doSomething(): Unit".isKotlinFunction()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinFunction passes on Kotlin function without return-type`() {
        val actual: Boolean = "fun doSomething()".isKotlinFunction()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinFunction fails on String missing 'fun' keyword`() {
        val actual: Boolean = "doSomething()".isKotlinFunction()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinFunction fails on String missing function name`() {
        val actual: Boolean = "fun ()".isKotlinFunction()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinFunction fails on String missing parenthesis for arguments`() {
        val actual: Boolean = "fun doSomething".isKotlinFunction()
        assertFalse(actual)
    }

    // --------------- String.isKotlinSingleExpressionFunction() ---------------

    @Test
    fun `isKotlinSingleExpressionFunction passes on Kotlin function with =`() {
        val actual: Boolean =
            "fun test() = Unit".isKotlinSingleExpressionFunction()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinSingleExpressionFunction fails on regular Kotlin function`() {
        val actual: Boolean = "fun test() {".isKotlinSingleExpressionFunction()
        assertFalse(actual)
    }

    // ---------------------- String.kotlinFunctionName() ----------------------

    @Test
    fun `kotlinFunctionName passes on Kotlin function`() {
        val expected = "doSomething"
        val actual: String = "fun $expected() {".kotlinFunctionName()
        assertEquals(expected, actual)
    }

    @Test
    fun `kotlinFunctionName fails on another String than Kotlin function`() {
        val text = "fun ()"
        val exception: IllegalArgumentException =
            assertFailsWith(block = text::kotlinFunctionName)
        val actual: String? = exception.message
        val expected = "String is not a function header (input: '$text')."
        assertEquals(expected, actual)
    }

    // ------------------ String.toKotlinMarkdownCodeBlock() -------------------

    @Test
    fun `toKotlinMarkdownCodeBlock passes on non-blank String`() {
        val text = "assert(true)"
        val actual: String = text.toKotlinMarkdownCodeBlock()
        val expected = """
            |```kotlin
            |$text
            |```
        """.trimMargin()
        assertEquals(expected, actual)
    }

    @Test
    fun `toKotlinMarkdownCodeBlock fails on blank String`() {
        val text = "  "
        val exception: IllegalArgumentException =
            assertFailsWith(block = text::toKotlinMarkdownCodeBlock)
        val actual: String? = exception.message
        val expected = "Blank string specified (input: '$text')."
        assertEquals(expected, actual)
    }

    // ---------------------- String.isJavaPublicClass() -----------------------

    @Test
    fun `isJavaPublicClass passes on Java class with 'public' modifier`() {
        val actual: Boolean = "public class A".isJavaPublicClass()
        assertTrue(actual)
    }

    @Test
    fun `isJavaPublicClass fails on String not being Java class`() {
        val actual: Boolean = "final int x = 1".isJavaPublicClass()
        assertFalse(actual)
    }

    @Test
    fun `isJavaPublicClass fails on Java class with package visibility`() {
        val actual: Boolean = "class A".isJavaPublicClass()
        assertFalse(actual)
    }

    @Test
    fun `isJavaPublicClass fails on Java class with private visibility`() {
        val actual: Boolean = "private class A".isJavaPublicClass()
        assertFalse(actual)
    }

    // ---------------------- String.isJavaTestFunction() ----------------------

    @Test
    fun `isJavaTestFunction passes on Java test function header`() {
        val actual: Boolean = "void doSomething() {".isJavaTestFunction()
        assertTrue(actual)
    }

    @Test
    fun `isJavaTestFunction fails on empty String`() {
        val actual: Boolean = "".isJavaTestFunction()
        assertFalse(actual)
    }

    @Test
    fun `isJavaTestFunction fails on blank String`() {
        val actual: Boolean = "   ".isJavaTestFunction()
        assertFalse(actual)
    }

    @Test
    fun `isJavaTestFunction fails on String missing 'void' return type`() {
        val actual: Boolean = "doSomething() {".isJavaTestFunction()
        assertFalse(actual)
    }

    @Test
    fun `isJavaTestFunction fails on String missing function name`() {
        val actual: Boolean = "void () {".isJavaTestFunction()
        assertFalse(actual)
    }

    @Test
    fun `isJavaTestFunction fails on String missing parenthesis for arguments`() {
        val actual: Boolean = "void doSomething {".isJavaTestFunction()
        assertFalse(actual)
    }

    @Test
    fun `isJavaTestFunction fails on String not ending with open-bracket`() {
        val actual: Boolean = "void doSomething()".isJavaTestFunction()
        assertFalse(actual)
    }
}
