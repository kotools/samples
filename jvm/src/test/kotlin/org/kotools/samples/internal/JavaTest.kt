package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JavaTest {
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
