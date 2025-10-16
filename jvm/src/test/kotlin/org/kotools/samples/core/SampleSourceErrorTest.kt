package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class SampleSourceErrorTest {
    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString returns message`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource =
            checkNotNull(KotlinSampleSource of file)
        val error: SampleSourceError =
            SampleSourceError.multipleClassesFoundIn(source)
        val actual: String = error.toString()
        val expected: String = error.message
        assertEquals(expected, actual)
    }

    // --------- Companion.multipleClassesFoundIn(KotlinSampleSource) ----------

    @Test
    fun `multipleClassesFoundIn passes with KotlinSampleSource`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource =
            checkNotNull(KotlinSampleSource of file)
        val actual: String = SampleSourceError.multipleClassesFoundIn(source)
            .message
        val expected = "Multiple classes found in ${source}."
        assertEquals(expected, actual)
    }

    // ---------- Companion.noPublicClassFoundIn(KotlinSampleSource) -----------

    @Test
    fun `noPublicClassFoundIn passes with KotlinSampleSource`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource =
            checkNotNull(KotlinSampleSource of file)
        val actual: String = SampleSourceError.noPublicClassFoundIn(source)
            .message
        val expected = "No public class found in ${source}."
        assertEquals(expected, actual)
    }

    // ----- Companion.singleExpressionFunctionFoundIn(KotlinSampleSource) -----

    @Test
    fun `singleExpressionFunctionFoundIn passes with KotlinSampleSource`() {
        val file = File("Sample.kt")
        val source: KotlinSampleSource =
            checkNotNull(KotlinSampleSource of file)
        val actual: String = SampleSourceError
            .singleExpressionFunctionFoundIn(source)
            .message
        val expected = "Single-expression function found in ${source}."
        assertEquals(expected, actual)
    }
}
