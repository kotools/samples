package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JavaTest {
    // --------------------- isPackageDeclaration(String) ----------------------

    @Test
    fun `isPackageDeclaration should pass with Java package declaration`() {
        val text = "package org.kotools.samples;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration should fail with missing package keyword`() {
        val text = "org.kotools.samples;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration should fail with missing package identifier`() {
        val text = "package;"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration should fail with missing semicolon`() {
        val text = "package org.kotools.samples"
        val actual: Boolean = Java()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }
}
