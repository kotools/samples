package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringTest {
    // ------------------------ String.isKotlinClass() -------------------------

    @Test
    fun `isKotlinClass passes on String with 'class' keyword and class name`() {
        val actual: Boolean = "class A".isKotlinClass()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinClass fails on String missing 'class' keyword`() {
        val actual: Boolean = "A".isKotlinClass()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinClass fails on String missing class name`() {
        val actual: Boolean = "class".isKotlinClass()
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
}
