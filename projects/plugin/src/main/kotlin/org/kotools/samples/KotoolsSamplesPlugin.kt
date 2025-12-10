package org.kotools.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Plugin that inlines read-only Kotlin samples into documentation, ensuring
 * they are always correct by compiling them alongside test sources.
 *
 * ### Motivation
 *
 * This plugin addresses a limitation in [Dokka](https://kotl.in/dokka), which
 * does not allow making code samples non-editable or non-executable.
 *
 * ### Key features
 *
 * - Adds `sample` directory in `test` Kotlin source set for isolating code
 * samples from main and test sources, and for compiling them alongside test
 * sources.
 * - Supports Kotlin/JVM projects.
 */
public class KotoolsSamplesPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val sampleDirectory: Directory =
                project.layout.projectDirectory.dir("src/sample/kotlin")
            project.extensions.kotlin(sampleDirectory)
            project.tasks.extractKotlinSamples(sampleDirectory)
        }

    private fun ExtensionContainer.kotlin(sampleDirectory: Directory) {
        this.getByType<KotlinJvmProjectExtension>()
            .sourceSets
            .named("test")
            .get()
            .kotlin
            .srcDir(sampleDirectory)
    }

    private fun TaskContainer.extractKotlinSamples(sourceDirectory: Directory) =
        this.register<ExtractKotlinSamplesTask>("extractKotlinSamples")
            .configure {
                this.description = "Extracts Kotlin samples from sources."
                this.group = "Kotools Samples"

                this.sourceDirectory.set(sourceDirectory)

                val extractedSamplesDirectory: Provider<Directory> = this
                    .project
                    .layout
                    .buildDirectory
                    .dir("kotools-samples/extracted")
                this.outputDirectory.set(extractedSamplesDirectory)
            }
}
