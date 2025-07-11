package org.kotools.samples.internal

import kotlin.test.Test
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
}
