package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinTest {
    // --------------------- isPackageDeclaration(String) ----------------------

    @Test
    fun `isPackageDeclaration should pass with Kotlin package declaration`() {
        val text = "package org.kotools.samples"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertTrue(actual)
    }

    @Test
    fun `isPackageDeclaration should fail with missing package keyword`() {
        val text = "org.kotools.samples"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }

    @Test
    fun `isPackageDeclaration should fail with missing package identifier`() {
        val text = "package"
        val actual: Boolean = Kotlin()
            .isPackageDeclaration(text)
        assertFalse(actual)
    }
}
