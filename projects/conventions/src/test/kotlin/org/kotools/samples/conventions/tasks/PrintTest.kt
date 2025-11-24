package org.kotools.samples.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import java.io.File
import kotlin.test.Test

class PrintTest {
    @Test
    fun `with message`() {
        // Given
        val project: File = gradleProject(
            """
                plugins { id("convention.help") }

                group = "org.kotools.samples"
                version = "0.0.0"
            """.trimIndent()
        )
        val task = "coordinates"
        // When
        val result: BuildResult = gradleBuild(project, task)
        // Then
        result.assertPrints("org.kotools.samples:${project.name}:0.0.0")
    }
}
