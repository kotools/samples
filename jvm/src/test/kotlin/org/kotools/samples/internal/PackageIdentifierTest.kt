package org.kotools.samples.internal

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PackageIdentifierTest {
    // ----------------------- Companion.orNull(String) ------------------------

    @Test
    fun `orNull passes with word having lowercase letters`() {
        val actual: PackageIdentifier? = PackageIdentifier.orNull("sample")
        assertNotNull(actual)
    }

    @Test
    fun `orNull passes with words having lowercase letters separated by dot`() {
        val actual: PackageIdentifier? = PackageIdentifier.orNull("my.sample")
        assertNotNull(actual)
    }

    @Test
    fun `orNull fails with empty text`() {
        val actual: PackageIdentifier? = PackageIdentifier.orNull("")
        assertNull(actual)
    }

    @Test
    fun `orNull fails with word not having only lowercase letters`() {
        val actual: PackageIdentifier? = PackageIdentifier.orNull("Sample1_")
        assertNull(actual)
    }

    @Test
    fun `orNull fails with words not having only lowercase letters`() {
        val actual: PackageIdentifier? = PackageIdentifier.orNull("My.Sample1_")
        assertNull(actual)
    }
}
