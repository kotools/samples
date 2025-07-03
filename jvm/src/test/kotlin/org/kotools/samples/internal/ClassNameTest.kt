package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClassNameTest {
    // ----------------------- Companion.orNull(String) ------------------------

    @Test
    fun `orNull passes with text containing only letters in Pascal case`() {
        val actual: ClassName? = ClassName.orNull("MyClass")
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails with empty text`() {
        val actual: ClassName? = ClassName.orNull("")
        assertNull(actual)
    }

    @Test
    fun `orNull fails with blank text`() {
        val actual: ClassName? = ClassName.orNull("   ")
        assertNull(actual)
    }

    @Test
    fun `orNull fails with text containing other characters than letters`() {
        val actual: ClassName? = ClassName.orNull("My-Class_123")
        assertNull(actual)
    }

    @Test
    fun `orNull fails with text using another convention than Pascal case`() {
        val actual: ClassName? = ClassName.orNull("myClass")
        assertNull(actual)
    }

    // ----------------------- Companion.orThrow(String) -----------------------

    @Test
    fun `orThrow passes with text containing only letters in Pascal case`() {
        ClassName.orThrow("MyClass")
    }

    @Test
    fun `orThrow fails with empty text`() {
        val exception: IllegalArgumentException = assertFailsWith {
            ClassName.orThrow("")
        }
        val actual: String? = exception.message
        val expected = "Class name must be non-empty."
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow fails with blank text`() {
        val exception: IllegalArgumentException = assertFailsWith {
            ClassName.orThrow("   ")
        }
        val actual: String? = exception.message
        val expected = "Class name must be non-blank."
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow fails with text containing other characters than letters`() {
        val text = "My-Class_123"
        val exception: IllegalArgumentException = assertFailsWith {
            ClassName.orThrow(text)
        }
        val actual: String? = exception.message
        val expected = "Class name must contain letters only (input: '$text')."
        assertEquals(expected, actual)
    }

    @Test
    fun `orThrow fails with text using another convention than Pascal case`() {
        val text = "myClass"
        val exception: IllegalArgumentException = assertFailsWith {
            ClassName.orThrow(text)
        }
        val actual: String? = exception.message
        val expected =
            "Class name must be written in Pascal case (input: '$text')."
        assertEquals(expected, actual)
    }
}
