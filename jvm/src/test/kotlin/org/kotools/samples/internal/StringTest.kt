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
}
