package org.kotools.samples.gradle.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import java.io.File
import kotlin.test.Test

class KotlinCompatibilityTest {
    @Test
    fun `with compatibility version`() {
        // Given
        val project: File = gradleProject(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                compatibility.kotlin = "2.0.21"
            """.trimIndent()
        )
        val task = "kotlinCompatibility"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("Kotlin 2.0.21 (api: 2.0, language: 2.0)")
    }

    @Test
    fun `without compatibility version`() {
        // Given
        val project: File = gradleProject(
            """
                import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                kotlin {
                    this.compilerOptions.apiVersion.set(KotlinVersion.KOTLIN_2_0)
                    this.compilerOptions.languageVersion.set(KotlinVersion.KOTLIN_1_9)
                    this.coreLibrariesVersion = "2.0.21"
                }
            """.trimIndent()
        )
        val task = "kotlinCompatibility"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("Kotlin 2.0.21 (api: 2.0, language: 1.9)")
    }
}
