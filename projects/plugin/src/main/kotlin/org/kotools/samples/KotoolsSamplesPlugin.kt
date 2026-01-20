package org.kotools.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
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
 * - Supports Kotlin/JVM projects.
 * - Adds `sample` directory in `test` Kotlin source set for isolating code
 * samples from main and test sources, and for compiling them alongside test
 * sources.
 * - Extracts Kotlin sample sources as Markdown files, ready to inline into
 * documentation (see [ExtractKotlinSamplesTask]).
 * - Inlines samples referenced from main sources (see [InlineSamplesTask]).
 */
public class KotoolsSamplesPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val sampleDirectory: Directory =
                project.layout.projectDirectory.dir("src/sample/kotlin")
            project.extensions.kotlin(sampleDirectory)
            val checkKotlinSamples: TaskProvider<CheckKotlinSamplesTask> =
                project.tasks.checkKotlinSamples(sampleDirectory)
            val extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask> =
                project.tasks.extractKotlinSamples(checkKotlinSamples)
            project.tasks.inlineSamples(extractKotlinSamples)
        }

    private fun ExtensionContainer.kotlin(sampleDirectory: Directory) {
        this.getByType<KotlinJvmProjectExtension>()
            .sourceSets
            .named("test")
            .get()
            .kotlin
            .srcDir(sampleDirectory)
    }

    private fun TaskContainer.checkKotlinSamples(
        sourceDirectory: Directory
    ): TaskProvider<CheckKotlinSamplesTask> {
        val task: TaskProvider<CheckKotlinSamplesTask> =
            this.register<CheckKotlinSamplesTask>("checkKotlinSamples")
        task.configure {
            this.description =
                "Checks the content of Kotlin samples from sources."
            this.group = "Kotools Samples"

            this.sourceDirectory.set(sourceDirectory)
        }
        return task
    }

    private fun TaskContainer.extractKotlinSamples(
        checkKotlinSamples: TaskProvider<CheckKotlinSamplesTask>
    ): TaskProvider<ExtractKotlinSamplesTask> {
        val task: TaskProvider<ExtractKotlinSamplesTask> =
            this.register<ExtractKotlinSamplesTask>("extractKotlinSamples")
        task.configure {
            this.description = "Extracts Kotlin samples from sources."
            this.group = "Kotools Samples"

            val sourceDirectory: Provider<Directory> =
                checkKotlinSamples.flatMap(
                    CheckKotlinSamplesTask::sourceDirectory
                )
            this.sourceDirectory.set(sourceDirectory)

            val extractedSamplesDirectory: Provider<Directory> = this.project
                .layout
                .buildDirectory
                .dir("kotools-samples/extracted")
            this.outputDirectory.set(extractedSamplesDirectory)

            this.dependsOn(checkKotlinSamples)
        }
        return task
    }

    private fun TaskContainer.inlineSamples(
        extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask>
    ): Unit = this.register<InlineSamplesTask>("inlineSamples")
        .configure {
            this.description = "Inlines Kotlin samples referenced from sources."
            this.group = "Kotools Samples"

            // Inputs:
            val sourceDirectory: Directory =
                this.project.layout.projectDirectory.dir("src/main/kotlin")
            this.sourceDirectory.set(sourceDirectory)
            val extractedSampleDirectory: Provider<Directory> =
                extractKotlinSamples
                    .flatMap(ExtractKotlinSamplesTask::outputDirectory)
            this.extractedSampleDirectory.set(extractedSampleDirectory)

            // Output:
            val outputDirectory: Provider<Directory> = this.project.layout
                .buildDirectory
                .dir("kotools-samples/inlined")
            this.outputDirectory.set(outputDirectory)
        }
}
