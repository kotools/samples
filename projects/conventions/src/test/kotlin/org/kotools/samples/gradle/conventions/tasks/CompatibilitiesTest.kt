package org.kotools.samples.gradle.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import java.io.File
import kotlin.test.Test

class CompatibilitiesTest {
    @Test
    fun `prints Java and Kotlin compatibilities`() {
        // Given
        val project: File = gradleProject(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                compatibility {
                    this.java = "17"
                    this.kotlin = "2.0.21"
                }
            """.trimIndent()
        )
        val task = "compatibilities"

        // When
        val result: BuildResult = gradleBuild(project, task)

        // Then
        result.assertPrints("Java 17")
        result.assertPrints("Kotlin 2.0.21 (api: 2.0, language: 2.0)")
    }
}
