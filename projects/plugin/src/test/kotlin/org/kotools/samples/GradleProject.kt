package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals
import kotlin.test.fail

internal class GradleProject private constructor(private val project: File) {
    companion object {
        fun create(): GradleProject {
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
            return GradleProject(project)
        }
    }

    fun mainSource(text: String): Unit = this.project
        .resolve("src/main/kotlin/TemporarySample.kt")
        .also(File::ensureParentDirsCreated)
        .writeText(text)

    fun sampleSource(text: String): Unit = this.project
        .resolve("src/sample/kotlin/TemporarySample.kt")
        .also(File::ensureParentDirsCreated)
        .writeText(text)

    fun successfulBuild(taskPath: String) {
        val result: BuildResult = GradleRunner.create()
            .withProjectDir(this.project)
            .withArguments(taskPath)
            .withPluginClasspath()
            .build()
        result.task(taskPath)
            ?.let { assertEquals(TaskOutcome.SUCCESS, it.outcome) }
            ?: fail("'$taskPath' Gradle task not found.")
    }

    fun failingBuild(taskPath: String): BuildResult {
        val result: BuildResult = GradleRunner.create()
            .withProjectDir(this.project)
            .withArguments(taskPath)
            .withPluginClasspath()
            .buildAndFail()
        result.task(taskPath)
            ?.let { assertEquals(TaskOutcome.FAILED, it.outcome) }
            ?: fail("'$taskPath' Gradle task not found.")
        return result
    }

    fun assertExtractedSample(path: String, expectedKotlin: String) {
        val actual: String = this.project
            .resolve("build/kotools-samples/extracted/$path")
            .readText()
        val expected = """
            |```kotlin
            |$expectedKotlin
            |```
        """.trimMargin()
        assertEquals(expected, actual)
    }
}
