package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringTest {
    // ---------------------- String.isClassDeclaration() ----------------------

    @Test
    fun `isClassDeclaration passes with 'class' keyword and class name in String`() {
        val actual: Boolean = "class Something".isClassDeclaration()
        assertTrue(actual)
    }

    @Test
    fun `isClassDeclaration fails without 'class' keyword in String`() {
        val actual: Boolean = "Something".isClassDeclaration()
        assertFalse(actual)
    }

    @Test
    fun `isClassDeclaration fails without class name in String`() {
        val actual: Boolean = "class".isClassDeclaration()
        assertFalse(actual)
    }

    // --------------- String.isPublicClassDeclarationInKotlin() ---------------

    @Test
    fun `isPublicClassDeclarationInKotlin passes on String starting with 'public class'`() {
        val actual: Boolean =
            "public class Something".isPublicClassDeclarationInKotlin()
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclarationInKotlin passes on String starting with 'class'`() {
        val actual: Boolean =
            "class Something".isPublicClassDeclarationInKotlin()
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclarationInKotlin fails on String starting with 'internal class'`() {
        val actual: Boolean =
            "internal class Something".isPublicClassDeclarationInKotlin()
        assertFalse(actual)
    }

    @Test
    fun `isPublicClassDeclarationInKotlin fails on String starting with 'private class'`() {
        val actual: Boolean =
            "private class Something".isPublicClassDeclarationInKotlin()
        assertFalse(actual)
    }

    // ---------------- String.isPublicClassDeclarationInJava() ----------------

    @Test
    fun `isPublicClassDeclarationInJava passes on String starting with 'public class'`() {
        val actual: Boolean =
            "public class Something {}".isPublicClassDeclarationInJava()
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclarationInJava fails on String starting with 'class'`() {
        val actual: Boolean =
            "class Something {}".isPublicClassDeclarationInJava()
        assertFalse(actual)
    }

    @Test
    fun `isPublicClassDeclarationInJava fails on String starting with 'private class'`() {
        val actual: Boolean =
            "private class Something {}".isPublicClassDeclarationInJava()
        assertFalse(actual)
    }
}
