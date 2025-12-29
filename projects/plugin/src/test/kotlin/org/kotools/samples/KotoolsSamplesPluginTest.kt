package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotoolsSamplesPluginTest {
    @Test
    fun `adds sample directory to test Kotlin source set for JVM projects`() {
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
    fun `registers extractKotlinSamples task for JVM projects`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        // When
        project.pluginManager.apply(KotoolsSamplesPlugin::class)

        // Then
        val task: ExtractKotlinSamplesTask = project.tasks
            .named<ExtractKotlinSamplesTask>("extractKotlinSamples")
            .get()
        assertEquals("Extracts Kotlin samples from sources.", task.description)
        assertEquals("Kotools Samples", task.group)
        val actualSourceDirectory: Directory = task.sourceDirectory.get()
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src/sample/kotlin")
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
        val actualOutputDirectory: Directory = task.outputDirectory.get()
        val expectedOutputDirectory: Directory = project.layout.buildDirectory
            .dir("kotools-samples/extracted")
            .get()
        assertEquals(expectedOutputDirectory, actualOutputDirectory)
    }

    @Test
    fun `registers inlineKotlinSamples task for JVM projects`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        // When
        project.pluginManager.apply(KotoolsSamplesPlugin::class)

        // Then
        val task: InlineKotlinSamplesTask = project.tasks
            .named<InlineKotlinSamplesTask>("inlineKotlinSamples")
            .get()
        assertEquals(
            "Inlines Kotlin samples referenced from sources.",
            task.description
        )
        assertEquals("Kotools Samples", task.group)

        val actualSourceDirectory: Directory = task.sourceDirectory.get()
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src/main/kotlin")
        assertEquals(expectedSourceDirectory, actualSourceDirectory)

        val actualExtractedSampleDirectory: Directory =
            task.extractedSampleDirectory.get()
        val expectedExtractedSampleDirectory: Directory = project.tasks
            .named<ExtractKotlinSamplesTask>("extractKotlinSamples")
            .flatMap(ExtractKotlinSamplesTask::outputDirectory)
            .get()
        assertEquals(
            expectedExtractedSampleDirectory,
            actualExtractedSampleDirectory
        )

        val actualOutputDirectory: Directory = task.outputDirectory.get()
        val expectedOutputDirectory: Directory = project.layout.buildDirectory
            .dir("kotools-samples/inlined")
            .get()
        assertEquals(expectedOutputDirectory, actualOutputDirectory)
    }
}
