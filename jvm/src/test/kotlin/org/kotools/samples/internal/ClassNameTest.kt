package org.kotools.samples.internal

import kotlin.test.Test
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
}
