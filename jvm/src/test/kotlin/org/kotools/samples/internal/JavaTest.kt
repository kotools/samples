package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JavaTest {
    // --------------------- isPackageDeclaration(String) ----------------------

    @Test
    fun `isPackageDeclaration passes with Java package declaration as text`() {
        val text = "package org.kotools.samples;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration fails without 'package' keyword in text`() {
        val text = "org.kotools.samples;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration fails without package identifier in text`() {
        val text = "package ;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration fails without semicolon at the end of text`() {
        val text = "package org.kotools.samples"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    // ---------------------- isClassDeclaration(String) -----------------------

    @Test
    fun `isClassDeclaration passes with 'class', name and bracket in text`() {
        val text = "class Something {"
        val actual: Boolean = Java()
            .isClassDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isClassDeclaration fails without 'class' keyword in text`() {
        val text = "Something {"
        val actual: Boolean = Java()
            .isClassDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isClassDeclaration fails without class name in text`() {
        val text = "class {"
        val actual: Boolean = Java()
            .isClassDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isClassDeclaration fails without open bracket in text`() {
        val text = "class Something"
        val actual: Boolean = Java()
            .isClassDeclaration(text)
        assertFalse(actual)
    }

    // ------------------- isPublicClassDeclaration(String) --------------------

    @Test
    fun `isPublicClassDeclaration passes with text starting with 'public class'`() {
        val text = "public class Something {"
        val actual: Boolean = Java()
            .isPublicClassDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPublicClassDeclaration fails with text starting with 'class'`() {
        val text = "class Something {"
        val actual: Boolean = Java()
            .isPublicClassDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPublicClassDeclaration fails with text starting with 'private class'`() {
        val text = "private class Something {"
        val actual: Boolean = Java()
            .isPublicClassDeclaration(text)
        assertFalse(actual)
    }

    // ------------------------ classNameOrNull(String) ------------------------

    @Test
    fun `classNameOrNull passes with Java class declaration as text`() {
        val expected = "Something"
        val actual: String? = Java()
            .classNameOrNull("class $expected {}")
        assertEquals(expected, actual)
    }

    @Test
    fun `classNameOrNull fails with another text than Java class declaration`() {
        val actual: String? = Java()
            .classNameOrNull("class")
        assertNull(actual)
    }
}
