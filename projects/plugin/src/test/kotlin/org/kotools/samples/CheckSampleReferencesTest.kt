package org.kotools.samples

import org.gradle.testkit.runner.BuildResult
import org.kotools.samples.internal.GradleProject
import org.kotools.samples.internal.assertOutputContains
import kotlin.test.Test

class CheckSampleReferencesTest {
    private val taskPath: String = ":checkSampleReferences"

    @Test
    fun `passes with empty main source`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource("")
        project.mainSource("")

        // When & Then
        project.successfulBuild(this.taskPath)
    }

    @Test
    fun `passes with reference to existing samples in main source`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition() {
                        val result: Int = addition(x = 1, y = 2)
                        check(result == 3)
                    }
                }
            """.trimIndent()
        )
        project.mainSource(
            """
                /** SAMPLE: [IntSample.addition] */
                fun addition(x: Int, y: Int): Int = x + y
            """.trimIndent()
        )

        // When & Then
        project.successfulBuild(this.taskPath)
    }

    @Test
    fun `fails when referenced sample doesn't exist`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition() {
                        val result: Int = addition(x = 1, y = 2)
                        check(result == 3)
                    }
                }
            """.trimIndent()
        )
        val sampleIdentifier = "IntSample.addition.oops"
        project.mainSource(
            """
                /** SAMPLE: [$sampleIdentifier] */
                fun addition(x: Int, y: Int): Int = x + y
            """.trimIndent()
        )

        // When
        val result: BuildResult = project.failingBuild(this.taskPath)

        // Then
        result.assertOutputContains("'$sampleIdentifier' sample not found.")
    }
}
