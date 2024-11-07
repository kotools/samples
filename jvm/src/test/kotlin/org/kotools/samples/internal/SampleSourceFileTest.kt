package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SampleSourceFileCompanionTest {
    @Test
    fun `orNull should pass with Java file in sample source set`() {
        val file = File("sample/java/Hello.java")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should pass with Kotlin file in sample source set`() {
        val file = File("sample/kotlin/Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file outside sample source set`() {
        val file = File("Hello.kt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with unsupported file in sample source set`() {
        val file = File("sample/Unsupported.txt")
        val actual: SampleSourceFile? = SampleSourceFile.orNull(file)
        assertNull(actual)
    }
}
