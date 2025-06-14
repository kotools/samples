package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KotlinTest {
    // --------------------- isPackageDeclaration(String) ----------------------

    @Test
    fun `isPackageDeclaration passes with Kotlin package declaration as text`() {
        val text = "package org.kotools.samples"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration fails without 'package' keyword in text`() {
        val text = "org.kotools.samples"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration fails without package identifier in text`() {
        val text = "package"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    // ---------------------- isClassDeclaration(String) -----------------------

    @Test
    fun `isClassDeclaration passes with 'class' and class name in text`() {
        val text = "class Something"
        val actual: Boolean = Kotlin()
            .isClassDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isClassDeclaration fails without 'class' keyword in text`() {
        val text = "val one = 1"
        val actual: Boolean = Kotlin()
            .isClassDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isClassDeclaration fails without class name in text`() {
        val text = "class"
        val actual: Boolean = Kotlin()
            .isClassDeclaration(text)
        assertFalse(actual)
    }

    // ------------------- isPublicClassDeclaration(String) --------------------

    @Test
    fun `isPublicClassDeclaration passes with text starting with 'public class'`() {
        val text = "public class Something"
        val actual: Boolean = Kotlin()
            .isPublicClassDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclaration passes with text starting with 'class'`() {
        val text = "class Something"
        val actual: Boolean = Kotlin()
            .isPublicClassDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclaration fails with text starting with 'internal class'`() {
        val text = "internal class Something"
        val actual: Boolean = Kotlin()
            .isPublicClassDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPublicClassDeclaration fails with text starting with 'private class'`() {
        val text = "private class Something"
        val actual: Boolean = Kotlin()
            .isPublicClassDeclaration(text)
        assertFalse(actual)
    }

    // ------------------------ classNameOrNull(String) ------------------------

    @Test
    fun `classNameOrNull passes with Kotlin class declaration as text`() {
        val expected = "Something"
        val actual: String? = Kotlin()
            .classNameOrNull("class $expected")
        assertEquals(expected, actual)
    }

    @Test
    fun `classNameOrNull fails with another text than Kotlin class declaration`() {
        val actual: String? = Kotlin()
            .classNameOrNull("class")
        assertNull(actual)
    }
}
