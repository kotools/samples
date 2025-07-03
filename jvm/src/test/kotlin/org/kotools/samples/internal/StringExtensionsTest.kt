package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringExtensionsTest {
    // ------------------------ String.isKotlinClass() -------------------------

    @Test
    fun `isKotlinClass passes on String with 'class' followed by name`() {
        val actual: Boolean = "class Sample".isKotlinClass()
        assertTrue(actual)
    }

    @Test
    fun `isKotlinClass fails on String missing 'class' keyword`() {
        val actual: Boolean = "Sample".isKotlinClass()
        assertFalse(actual)
    }

    @Test
    fun `isKotlinClass fails on String missing class name`() {
        val actual: Boolean = "class".isKotlinClass()
        assertFalse(actual)
    }
}
