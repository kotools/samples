package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinTest {
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
}
