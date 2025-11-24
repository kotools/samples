package org.kotools.samples.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertTrue

class JavaCompatibilityTest {
    @Test
    fun `saves compatibility version in output file`() {
        // Given
        val version = 17
        val runner = GradleRunner { """compatibility.java = "$version"""" }
        // When
        val result: BuildResult = runner.build()
        // Then
        assertTrue("Java $version" in result.output)
    }

    @Test
    fun `saves same source and target compatibilities in output file`() {
        // Given
        val version = 17
        val runner = GradleRunner {
            """
                tasks.withType<JavaCompile>().configureEach {
                    this.sourceCompatibility = "$version"
                    this.targetCompatibility = "$version"
                }
            """.trimIndent()
        }
        // When
        val result: BuildResult = runner.build()
        // Then
        assertTrue("Java $version" in result.output)
    }

    @Test
    fun `saves different source and target compatibilities in output file`() {
        // Given
        val sourceVersion = 21
        val targetVersion = 20
        val runner = GradleRunner {
            """
                tasks.withType<JavaCompile>().configureEach {
                    this.sourceCompatibility = "$sourceVersion"
                    this.targetCompatibility = "$targetVersion"
                }
            """.trimIndent()
        }
        // When
        val result: BuildResult = runner.build()
        // Then
        val expected =
            "Java $sourceVersion (source) and $targetVersion (target)"
        assertTrue(expected in result.output)
    }
}

@Suppress("TestFunctionName")
private fun GradleRunner(buildConfiguration: () -> String): GradleRunner {
    val directory: File = createTempDirectory()
        .toFile()
    File(directory, "settings.gradle.kts")
        .writeText("")
    File(directory, "build.gradle.kts")
        .writeText(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                ${buildConfiguration()}
            """.trimIndent()
        )
    return GradleRunner.create()
        .withProjectDir(directory)
        .withArguments("javaCompatibility")
        .withPluginClasspath()
}
