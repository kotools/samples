package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import kotlin.test.Test

class CheckKotlinSamplesTest {
    private val taskPath: String = ":checkKotlinSamples"

    @Test
    fun `passes with empty sample source`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource("")

        // When & Then
        project.successfulBuild(this.taskPath)
    }

    @Test
    fun `passes without top-level function in sample source`() {
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
    }

    @Test
    fun `fails with top-level function found in sample source`() {
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
