package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KotlinSampleSourceFileCompanionTest {
    // ----------------------------- orNull(File) ------------------------------

    @Test
    fun `orNull should pass with Kotlin test file having right suffix`() {
        val file = File("test/kotlin/ValidSample.kt")
        val actual: KotlinSampleSourceFile? =
            KotlinSampleSourceFile.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull should fail with file using another language than Kotlin`() {
        val file = File("test/java/InvalidSample.java")
        val actual: KotlinSampleSourceFile? =
            KotlinSampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with Kotlin test file having wrong suffix`() {
        val file = File("test/kotlin/Invalid.kt")
        val actual: KotlinSampleSourceFile? =
            KotlinSampleSourceFile.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull should fail with Kotlin production file`() {
        val file = File("main/kotlin/Sample.kt")
        val actual: KotlinSampleSourceFile? =
            KotlinSampleSourceFile.orNull(file)
        assertNull(actual)
    }
}
