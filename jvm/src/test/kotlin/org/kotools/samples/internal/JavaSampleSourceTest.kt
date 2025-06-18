package org.kotools.samples.internal

import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JavaSampleSourceTest {
    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Java file having name suffixed by 'Sample'`() {
        val file = File("Sample.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails without 'Sample' suffix in Java file's name`() {
        val file = File("Test.java")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun `orNull fails with file's extension other than 'java'`() {
        val file = File("Sample.kt")
        val actual: JavaSampleSource? = JavaSampleSource.orNull(file)
        assertNull(actual)
    }
}
