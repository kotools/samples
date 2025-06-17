package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KotlinSampleSourceTest {
    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Kotlin file having name suffixed by 'Sample'`() {
        val file = File("Sample.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Kotlin file's name`() {
        val file = File("Test.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull fails with file's extension other than 'kt'`() {
        val file = File("Sample.java")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }
}
