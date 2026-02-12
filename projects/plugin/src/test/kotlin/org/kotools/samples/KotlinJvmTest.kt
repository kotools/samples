package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.kotools.samples.internal.GradleProject
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class KotlinJvmTest {
    @Test
    fun `adds sample directory to test Kotlin source set`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        // When
        project.pluginManager.apply(KotoolsSamplesPlugin::class)

        // Then
        val sourceDirectories: FileCollection = project.extensions
            .getByType<KotlinJvmProjectExtension>()
            .sourceSets
            .named("test")
            .get()
            .kotlin
            .sourceDirectories
        val expected: File = project.layout.projectDirectory
            .dir("src/sample/kotlin")
            .asFile
        val message = "Directory not found in Kotlin sources (was: $expected)."
        assertTrue(expected in sourceDirectories, message)
    }

    @Test
    fun `inlines samples in sources JAR`() {
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

        // When
        project.successfulBuild(taskPath = ":kotlinSourcesJar")

        // Then
        project.assertInlinedMainSourceJar(
            """
                /**
                 * ```kotlin
                 * val result: Int = addition(x = 1, y = 2)
                 * check(result == 3)
                 * ```
                 *
                 * _(sample integrated with 💚 by [Kotools Samples](https://github.com/kotools/samples))_
                 */
                fun addition(x: Int, y: Int): Int = x + y
            """.trimIndent()
        )
    }
}
