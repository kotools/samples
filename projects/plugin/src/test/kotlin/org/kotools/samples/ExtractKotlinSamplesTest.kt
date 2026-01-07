package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import kotlin.test.Test

class ExtractKotlinSamplesTest {
    private val taskPath: String = ":extractKotlinSamples"

    @Test
    fun `extracts body of member function`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
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

        // When
        project.successfulBuild(this.taskPath)

        // Then
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
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }
            """.trimIndent()
        )

        // When
        project.successfulBuild(this.taskPath)

        // Then
        project.assertExtractedSample(
            path = "IntSample/addition.md",
            expectedKotlin = "check(1 + 2 == 3)"
        )
    }

    @Test
    fun `extracts placeholder on member function with empty body`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition() {}
                }
            """.trimIndent()
        )

        // When
        project.successfulBuild(this.taskPath)

        // Then
        project.assertExtractedSample(
            path = "IntSample/addition.md",
            expectedKotlin = """TODO("Sample is not yet implemented.")"""
        )
    }

    @Test
    fun `extracts member function with package`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                package test

                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }
            """.trimIndent()
        )

        // When
        project.successfulBuild(this.taskPath)

        // Then
        project.assertExtractedSample(
            path = "test/IntSample/addition.md",
            expectedKotlin = "check(1 + 2 == 3)"
        )
    }

    @Test
    fun `extracts member functions from multiple classes`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }

                class LongSample {
                    fun addition(): Unit = check(1L + 2L == 3L)
                }
            """.trimIndent()
        )

        // When
        project.successfulBuild(this.taskPath)

        // Then
        project.assertExtractedSample(
            path = "IntSample/addition.md",
            expectedKotlin = "check(1 + 2 == 3)"
        )
        project.assertExtractedSample(
            path = "LongSample/addition.md",
            expectedKotlin = "check(1L + 2L == 3L)"
        )
    }

    @Test
    fun `fails on top-level function`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource("fun addition(): Unit = check(1 + 2 == 3)")

        // When
        val result: BuildResult = project.failingBuild(this.taskPath)

        // Then
        result.assertOutputContains(
            "Top-level function found in Kotlin sample source."
        )
    }
}
