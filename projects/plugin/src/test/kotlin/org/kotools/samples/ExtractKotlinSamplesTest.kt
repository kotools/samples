package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

// ----------------------------------- Tests -----------------------------------

class ExtractKotlinSamplesTest {
    @Test
    fun `extracts body of member function`() {
        // Given
        val project: File = projectWithSampleSource {
            """
                class IntSample {
                    fun addition() {
                        val x = 1
                        val y = 2
                        check(x + y == 3)
                    }
                }
            """.trimIndent()
        }

        // When
        val result: BuildResult = GradleRunner(project)
            .build()

        // Then
        result.assertTaskOutcome(TaskOutcome.SUCCESS)
        project.assertExtractedSample(
            path = "IntSample/addition.md",
            expectedKotlin = """
                val x = 1
                val y = 2
                check(x + y == 3)
            """.trimIndent()
        )
    }

    @Test
    fun `extracts expression body of member function`() {
        // Given
        val project: File = projectWithSampleSource {
            """
                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }
            """.trimIndent()
        }

        // When
        val result: BuildResult = GradleRunner(project)
            .build()

        // Then
        result.assertTaskOutcome(TaskOutcome.SUCCESS)
        project.assertExtractedSample(
            path = "IntSample/addition.md",
            expectedKotlin = "check(1 + 2 == 3)"
        )
    }

    @Test
    fun `extracts member function with package`() {
        // Given
        val project: File = projectWithSampleSource {
            """
                package test

                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }
            """.trimIndent()
        }

        // When
        val result: BuildResult = GradleRunner(project)
            .build()

        // Then
        result.assertTaskOutcome(TaskOutcome.SUCCESS)
        project.assertExtractedSample(
            path = "test/IntSample/addition.md",
            expectedKotlin = "check(1 + 2 == 3)"
        )
    }

    // TODO: extracts member functions from multiple classes

    // TODO: extracts placeholder on member function with blank body
    // Placeholder: TODO("Sample is not yet implemented.")

    @Test
    fun `fails on top-level function`() {
        // Given
        val project: File = projectWithSampleSource {
            "fun addition(): Unit = check(1 + 2 == 3)"
        }

        // When
        val result: BuildResult = GradleRunner(project)
            .buildAndFail()

        // Then
        result.assertTaskOutcome(TaskOutcome.FAILED)
        assertTrue(
            "Top-level function found in Kotlin sample source." in result.output
        )
    }
}

// ------------------------------ Private helpers ------------------------------

private const val TASK_PATH: String = ":extractKotlinSamples"

private fun projectWithSampleSource(text: () -> String): File {
    val project: File = createTempDirectory()
        .toFile()
    project.resolve("settings.gradle.kts")
        .writeText("")
    project.resolve("build.gradle.kts")
        .writeText(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("org.kotools.samples")
                }
            """.trimIndent()
        )
    project.resolve("src/sample/kotlin/TemporarySample.kt")
        .also(File::ensureParentDirsCreated)
        .writeText(text())
    return project
}

@Suppress("TestFunctionName")
private fun GradleRunner(project: File): GradleRunner = GradleRunner.create()
    .withProjectDir(project)
    .withArguments(TASK_PATH)
    .withPluginClasspath()

private fun BuildResult.assertTaskOutcome(expected: TaskOutcome): Unit = this
    .task(TASK_PATH)
    ?.let { assertEquals(expected, actual = it.outcome) }
    ?: fail("'$TASK_PATH' Gradle task not found.")

private fun File.assertExtractedSample(path: String, expectedKotlin: String) {
    val actual: String = this.resolve("build/kotools-samples/extracted/$path")
        .readText()
    val expected = """
        |```kotlin
        |$expectedKotlin
        |```
    """.trimMargin()
    assertEquals(expected, actual)
}
