package org.kotools.samples.internal

import java.io.File
import java.net.URI
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

class KotlinSampleSourceTest {
    // ----------------------- checkSinglePublicClass() ------------------------

    @Test
    fun `checkSinglePublicClass passes with single public class in file`() {
        val path = "kotlin/SinglePublicClassSample.kt"
        val url: URL = this::class.java.getResource("/$path")
            ?: fail("Resource file not found at '$path'.")
        val uri: URI = url.toURI()
        val file = File(uri)
        KotlinSampleSource.orNull(file)
            ?.checkSinglePublicClass()
            ?: fail("'$file' is not a Kotlin sample source.")
    }

    @Test
    fun `checkSinglePublicClass fails with no public class in file`() {
        val path = "kotlin/NoPublicClassSample.kt"
        val url: URL = this::class.java.getResource("/$path")
            ?: fail("Resource file not found at '$path'.")
        val uri: URI = url.toURI()
        val file = File(uri)
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orNull(file)
            ?: fail("'$file' is not a Kotlin sample source.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected = "No public class found in '$sampleSource'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with multiple public classes in file`() {
        val path = "kotlin/MultiplePublicClassesSample.kt"
        val url: URL = this::class.java.getResource("/$path")
            ?: fail("Resource file not found at '$path'.")
        val uri: URI = url.toURI()
        val file = File(uri)
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orNull(file)
            ?: fail("'$file' is not a Kotlin sample source.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected = "Multiple public classes found in '$sampleSource'."
        assertEquals(expected, actual)
    }

    @Test
    fun `checkSinglePublicClass fails with nested public class in file`() {
        val path = "kotlin/NestedPublicClassSample.kt"
        val url: URL = this::class.java.getResource("/$path")
            ?: fail("Resource file not found at '$path'.")
        val uri: URI = url.toURI()
        val file = File(uri)
        val sampleSource: KotlinSampleSource = KotlinSampleSource.orNull(file)
            ?: fail("'$file' is not a Kotlin sample source.")
        val actual: String? = assertFailsWith<IllegalStateException>(
            block = sampleSource::checkSinglePublicClass
        ).message
        val expected = "Multiple public classes found in '$sampleSource'."
        assertEquals(expected, actual)
    }

    // ------------------------------ toString() -------------------------------

    @Test
    fun `toString passes`() {
        val file = File("test/kotlin/Sample.kt")
        val actual: String = KotlinSampleSource.orNull(file)
            ?.toString()
            ?: fail("'$file' is not a Kotlin sample source.")
        val expected: String = file.toString()
        assertEquals(expected, actual)
    }

    // ------------------------ Companion.orNull(File) -------------------------

    @Test
    fun `orNull passes with Kotlin sample source file`() {
        val file = File("test/kotlin/Sample.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails with file extension other than 'kt'`() {
        val file = File("test/kotlin/Sample.kts")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull fails with file outside of test Kotlin source set`() {
        val file = File("main/kotlin/Sample.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }

    @Test
    fun `orNull fails with file name not suffixed by 'Sample'`() {
        val file = File("test/kotlin/File.kt")
        val actual: KotlinSampleSource? = KotlinSampleSource.orNull(file)
        assertNull(actual)
    }
}
