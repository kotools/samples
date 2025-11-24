package org.kotools.samples.conventions.tasks

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.assertTrue

// ----------------------------------- Setup -----------------------------------

internal fun gradleProject(buildConfiguration: String): File {
    val directory: File = createTempDirectory()
        .toFile()
    File(directory, "settings.gradle.kts")
        .writeText("")
    File(directory, "build.gradle.kts")
        .writeText(buildConfiguration)
    return directory
}

internal fun gradleBuild(project: File, task: String): BuildResult =
    GradleRunner.create()
        .withProjectDir(project)
        .withArguments(task)
        .withPluginClasspath()
        .build()

// -------------------------------- Assertions ---------------------------------

internal fun BuildResult.assertPrints(expected: String): Unit = assertTrue(
    expected in this.output,
    "Gradle build should print \"$expected\", but was: ${this.output}"
)
