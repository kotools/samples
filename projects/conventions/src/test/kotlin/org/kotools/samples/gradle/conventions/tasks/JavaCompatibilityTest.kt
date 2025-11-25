package org.kotools.samples.gradle.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import java.io.File
import kotlin.test.Test

class JavaCompatibilityTest {
    @Test
    fun `with compatibility version`() {
        // Given
        val project: File = gradleProject(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                compatibility.java = "17"
            """.trimIndent()
        )
        val task = "javaCompatibility"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("Java 17")
    }

    @Test
    fun `with same source and target compatibilities`() {
        // Given
        val project: File = gradleProject(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                tasks.withType<JavaCompile>().configureEach {
                    this.sourceCompatibility = "17"
                    this.targetCompatibility = "17"
                }
            """.trimIndent()
        )
        val task = "javaCompatibility"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("Java 17")
    }

    @Test
    fun `with different source and target compatibilities`() {
        // Given
        val project: File = gradleProject(
            """
                plugins {
                    id("org.jetbrains.kotlin.jvm")
                    id("convention.compatibility")
                }

                tasks.withType<JavaCompile>().configureEach {
                    this.sourceCompatibility = "21"
                    this.targetCompatibility = "20"
                }
            """.trimIndent()
        )
        val task = "javaCompatibility"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("Java 21 (target: 20)")
    }
}
