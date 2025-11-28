package org.kotools.samples.conventions

import org.gradle.testkit.runner.BuildResult
import org.kotools.samples.conventions.internal.assertPrints
import org.kotools.samples.conventions.internal.gradleBuild
import org.kotools.samples.conventions.internal.gradleProject
import java.io.File
import kotlin.test.Test

class CoordinatesTaskTest {
    @Test
    fun `prints coordinates to standard output`() {
        // Given
        val projectGroup = "org.kotools"
        val projectName = "test"
        val projectVersion = "0.0.0"
        val project: File = gradleProject(
            settingsConfiguration = """rootProject.name = "$projectName"""",
            buildConfiguration = """
                plugins { id("org.kotools.samples.conventions.coordinates") }

                group = "$projectGroup"
                version = "$projectVersion"
            """.trimIndent()
        )
        val task = "coordinates"

        // When
        val result: BuildResult = gradleBuild(project, task)

        // Then
        result.assertPrints("$projectGroup:$projectName:$projectVersion")
    }
}
