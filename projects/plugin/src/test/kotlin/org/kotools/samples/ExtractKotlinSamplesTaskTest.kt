package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ExtractKotlinSamplesTaskTest {
    @Test
    fun `member function with body`() {
        // Given
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
        project.resolve("src/sample/kotlin/IntegerSamples.kt")
            .also(File::ensureParentDirsCreated)
            .writeText(
                """
                    class IntSample {
                        fun addition() {
                            val x = 1
                            val y = 2
                            check(x + y == 3)
                        }
                    }
                """.trimIndent()
            )
        val taskPath = ":extractKotlinSamples"

        // When
        val result: BuildResult = GradleRunner.create()
            .withProjectDir(project)
            .withArguments(taskPath)
            .withPluginClasspath()
            .build()

        // Then
        result.task(taskPath)
            ?.let { assertEquals(TaskOutcome.SUCCESS, it.outcome) }
            ?: fail("'$taskPath' Gradle task not found.")
        project.resolve("build/kotools-samples/extracted/IntSample/addition.md")
            .readText()
            .let { actual: String ->
                val expected: String = """
                        ```kotlin
                        val x = 1
                        val y = 2
                        check(x + y == 3)
                        ```
                    """.trimIndent()
                assertEquals(expected, actual)
            }
    }
    // TODO: single-expression member function
    // TODO: top-level function
    // TODO: function in package
}
