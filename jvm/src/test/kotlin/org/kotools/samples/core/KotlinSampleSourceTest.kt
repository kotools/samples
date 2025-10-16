package org.kotools.samples.core

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
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

    // ----------------------- isClassOrFunction(String) -----------------------

    @Test
    fun isClassOrFunctionPassesWithClassKotlinKeywordInLine(): Unit =
        assertTrue { KotlinSampleSource.isClassOrFunction(line = "class A") }

    @Test
    fun isClassOrFunctionPassesWithFunKotlinKeywordInLine(): Unit =
        assertTrue {
            KotlinSampleSource.isClassOrFunction(line = "fun test() = Unit")
        }

    @Test
    fun isClassOrFunctionFailsWithoutClassAndFunKotlinKeywordsInLine(): Unit =
        assertFalse {
            KotlinSampleSource.isClassOrFunction(line = "val one: Int = 1")
        }

    // ---------------- classFunctionError(File, List<String>) -----------------

    @Test
    fun classFunctionErrorPassesWithSinglePublicClassInDeclarations() {
        val file = File("Sample.kt")
        val declarations: List<String> = listOf(
            "class IntegersKotlinSample {",
            "fun takeIfPositive() {"
        )
        val actual: String? =
            KotlinSampleSource.classFunctionError(file, declarations)
        assertNull(actual)
    }

    @Test
    fun classFunctionErrorFailsWithMultipleClassesInDeclarations() {
        val file = File("Sample.kt")
        val declarations: List<String> = listOf(
            "class FirstClass {",
            "class SecondClass {"
        )
        val actual: String? =
            KotlinSampleSource.classFunctionError(file, declarations)
        val expected =
            "Multiple classes found in '${file.name}' Kotlin sample source."
        assertEquals(expected, actual)
    }

    @Test
    fun classFunctionErrorFailsWithNoPublicClassInDeclarations() {
        val file = File("Sample.kt")
        val declarations: List<String> = listOf(
            "internal class InternalClass {"
        )
        val actual: String? =
            KotlinSampleSource.classFunctionError(file, declarations)
        val expected =
            "No public class found in '${file.name}' Kotlin sample source."
        assertEquals(expected, actual)
    }

    @Test
    fun classFunctionErrorFailsWithSingleExpressionFunctionInDeclarations() {
        val file = File("Sample.kt")
        val declarations: List<String> = listOf(
            "class Sample {",
            "fun singleExpressionTest() = Unit"
        )
        val actual: String? =
            KotlinSampleSource.classFunctionError(file, declarations)
        val expected = "Single-expression function found in '${file.name}' " +
                "Kotlin sample source."
        assertEquals(expected, actual)
    }
}
