package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StringTest {
    // --------------------- String.isPackageDeclaration() ---------------------

    @Test
    fun `isPackageDeclaration passes with package declaration`() {
        val actual: Boolean = "package org.samples".isPackageDeclaration()
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration passes with package declaration and semicolon`() {
        val actual: Boolean = "package org.samples;".isPackageDeclaration()
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration fails without 'package' keyword in String`() {
        val actual: Boolean = "org.samples".isPackageDeclaration()
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration fails without package identifier in String`() {
        val actual: Boolean = "package".isPackageDeclaration()
        assertFalse(actual)
    }

    // ------------------- String.packageIdentifierOrNull() --------------------

    @Test
    fun `packageIdentifierOrNull passes on package declaration as String`() {
        val expected = "org.samples"
        val actual: String? = "package $expected".packageIdentifierOrNull()
        assertEquals(expected, actual)
    }

    @Test
    fun `packageIdentifierOrNull fails on another String than package declaration`() {
        val actual: String? = "package".packageIdentifierOrNull()
        assertNull(actual)
    }

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

    // ----------------------- String.classNameOrNull() ------------------------

    @Test
    fun `classNameOrNull passes on class declaration`() {
        val expected = "Something"
        val actual: String? = "class $expected".classNameOrNull()
        assertEquals(expected, actual)
    }

    @Test
    fun `classNameOrNull fails on another String than class declaration`() {
        val actual: String? = "class".classNameOrNull()
        assertNull(actual)
    }

    // ------------------- String.isTestFunctionHeaderInKotlin() -------------------

    @Test
    fun `isTestFunctionHeaderInKotlin passes on Kotlin function declaration`() {
        val actual: Boolean =
            "fun helloWorld() {".isTestFunctionHeaderInKotlin()
        assertTrue(actual)
    }

    @Test
    fun `isTestFunctionHeaderInKotlin fails on String missing 'fun' keyword`() {
        val actual: Boolean = "helloWorld() {".isTestFunctionHeaderInKotlin()
        assertFalse(actual)
    }

    @Test
    fun `isTestFunctionHeaderInKotlin fails on String missing function name`() {
        val actual: Boolean = "fun () {".isTestFunctionHeaderInKotlin()
        assertFalse(actual)
    }

    // -------------------- String.isTestFunctionHeaderInJava() --------------------

    @Test
    fun `isTestFunctionHeaderInJava passes on Java function declaration`() {
        val actual: Boolean = "void helloWorld() {".isTestFunctionHeaderInJava()
        assertTrue(actual)
    }

    @Test
    fun `isTestFunctionHeaderInJava fails on String missing 'void' return type`() {
        val actual: Boolean = "helloWorld() {".isTestFunctionHeaderInJava()
        assertFalse(actual)
    }

    @Test
    fun `isTestFunctionHeaderInJava fails on String missing function name`() {
        val actual: Boolean = "void () {".isTestFunctionHeaderInJava()
        assertFalse(actual)
    }

    // ---------------- String.testFunctionNameInKotlinOrNull() ----------------

    @Test
    fun `testFunctionNameInKotlinOrNull passes on Kotlin test function header`() {
        val expected = "helloWorld"
        val actual: String? =
            "fun $expected() {".testFunctionNameInKotlinOrNull()
        assertEquals(expected, actual)
    }

    @Test
    fun `testFunctionNameInKotlinOrNull fails on another String than Kotlin test function header`() {
        val actual: String? = "fun".testFunctionNameInKotlinOrNull()
        assertNull(actual)
    }

    // ----------------- String.testFunctionNameInJavaOrNull() -----------------

    @Test
    fun `testFunctionNameInJavaOrNull passes on Java test function header`() {
        val expected = "helloWorld"
        val actual: String? =
            "void $expected() {".testFunctionNameInJavaOrNull()
        assertEquals(expected, actual)
    }

    @Test
    fun `testFunctionNameInJavaOrNull fails on another String than Java test function header`() {
        val actual: String? = "void".testFunctionNameInJavaOrNull()
        assertNull(actual)
    }
}
