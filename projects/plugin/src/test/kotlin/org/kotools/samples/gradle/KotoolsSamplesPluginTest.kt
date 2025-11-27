package org.kotools.samples.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class KotoolsSamplesPluginTest {
    @Test
    fun `adds sample directory to test Kotlin source set for Kotlin JVM`() {
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
        assertTrue(expected in sourceDirectories)
    }
}
