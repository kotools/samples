package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KotlinSampleSourceTest {
    // ----------------------------- isValid(File) -----------------------------

    @Test
    fun `isValid passes with sample suffix and kt extension in file name`() {
        val file = File("Sample.kt")
        assertTrue { KotlinSampleSource.isValid(file) }
    }

    @Test
    fun `isValid fails without sample suffix in file name`() {
        val file = File("Test.kt")
        assertFalse { KotlinSampleSource.isValid(file) }
    }

    @Test
    fun `isValid fails without kt extension in file name`() {
        val file = File("Sample.java")
        assertFalse { KotlinSampleSource.isValid(file) }
    }

    // ---------------------------- isClass(String) ----------------------------

    @Test
    fun `isClass passes with class Kotlin keyword in line`(): Unit =
        assertTrue { KotlinSampleSource.isClass(line = "class A") }

    @Test
    fun `isClass fails without class Kotlin keyword in line`(): Unit =
        assertFalse { KotlinSampleSource.isClass(line = "val one: Int = 1") }

    // -------------------------- isFunction(String) ---------------------------

    @Test
    fun `isFunction passes with fun Kotlin keyword in line`(): Unit =
        assertTrue { KotlinSampleSource.isFunction(line = "fun test() = Unit") }

    @Test
    fun `isFunction fails without fun Kotlin keyword in line`(): Unit =
        assertFalse { KotlinSampleSource.isFunction(line = "val one: Int = 1") }
}
