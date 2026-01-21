package org.kotools.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
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
 * - Checks the content of Kotlin sample sources (see [CheckKotlinSamplesTask]).
 * - Extracts Kotlin sample sources as Markdown files, ready to inline into
 * documentation (see [ExtractKotlinSamplesTask]).
 * - Checks the existence of samples referenced from main sources (see
 * [CheckSampleReferencesTask]).
 * - Inlines samples referenced from main sources (see [InlineSamplesTask]).
 * - Integrates main sources with inlined samples in sources JAR.
 */
public class KotoolsSamplesPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val sampleDirectory: Directory =
                project.layout.projectDirectory.dir("src/sample/kotlin")
            val kotlin: KotlinJvmProjectExtension =
                project.extensions.kotlin(sampleDirectory)
            val checkKotlinSamples: TaskProvider<CheckKotlinSamplesTask> =
                project.tasks.checkKotlinSamples(sampleDirectory)
            val extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask> =
                project.tasks.extractKotlinSamples(checkKotlinSamples)
            val checkSampleReferences: TaskProvider<CheckSampleReferencesTask> =
                project.tasks.checkSampleReferences(extractKotlinSamples)
            project.tasks.check(checkSampleReferences)
            val inlineSamples: TaskProvider<InlineSamplesTask> =
                project.tasks.inlineSamples(checkSampleReferences)
            project.tasks.jarTasks(kotlin, inlineSamples)
            project.tasks.dokka(inlineSamples)
        }

    private fun ExtensionContainer.kotlin(
        sampleDirectory: Directory
    ): KotlinJvmProjectExtension {
        val kotlin: KotlinJvmProjectExtension = this.getByType()
        kotlin.sourceSets.named("test")
            .get()
            .kotlin
            .srcDir(sampleDirectory)
        return kotlin
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

            // Inputs:
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
            this.dependsOn(checkKotlinSamples)

            // Inputs:
            checkKotlinSamples.flatMap(CheckKotlinSamplesTask::sourceDirectory)
                .let(this.sourceDirectory::set)

            // Outputs:
            this.project.layout.buildDirectory.dir("kotools-samples/extracted")
                .let(this.outputDirectory::set)
        }
        return task
    }

    private fun TaskContainer.checkSampleReferences(
        extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask>
    ): TaskProvider<CheckSampleReferencesTask> {
        val task: TaskProvider<CheckSampleReferencesTask> =
            this.register<CheckSampleReferencesTask>("checkSampleReferences")
        task.configure {
            this.description =
                "Checks the existence of samples referenced from sources."
            this.group = "Kotools Samples"

            // Inputs:
            this.project.layout.projectDirectory.dir("src/main/kotlin")
                .let(this.sourceDirectory::set)
            extractKotlinSamples
                .flatMap(ExtractKotlinSamplesTask::outputDirectory)
                .let(this.extractedSampleDirectory::set)
        }
        return task
    }

    private fun TaskContainer.check(
        checkSampleReferences: TaskProvider<CheckSampleReferencesTask>
    ): Unit = this.named("check")
        .configure { this.dependsOn(checkSampleReferences) }

    private fun TaskContainer.inlineSamples(
        checkSampleReferences: TaskProvider<CheckSampleReferencesTask>
    ): TaskProvider<InlineSamplesTask> {
        val task: TaskProvider<InlineSamplesTask> =
            this.register<InlineSamplesTask>("inlineSamples")
        task.configure {
            this.description =
                "Inlines Kotlin samples referenced from sources."
            this.group = "Kotools Samples"
            this.dependsOn(checkSampleReferences)

            // Inputs:
            checkSampleReferences
                .flatMap(CheckSampleReferencesTask::sourceDirectory)
                .let(this.sourceDirectory::set)
            checkSampleReferences
                .flatMap(CheckSampleReferencesTask::extractedSampleDirectory)
                .let(this.extractedSampleDirectory::set)

            // Output:
            this.project.layout.buildDirectory.dir("kotools-samples/inlined")
                .let(this.outputDirectory::set)
        }
        return task
    }

    private fun TaskContainer.jarTasks(
        kotlin: KotlinJvmProjectExtension,
        inlineSamples: TaskProvider<InlineSamplesTask>
    ): Unit = this.withType<Jar>()
        .named { it.endsWith("sourcesJar", ignoreCase = true) }
        .configureEach {
            this.dependsOn(inlineSamples)
            this.doFirst {
                kotlin.sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME)
                    .configure {
                        this@doFirst.project.layout.buildDirectory
                            .dir("kotools-samples/inlined")
                            .let { this.kotlin.setSrcDirs(listOf(it)) }
                    }
            }
        }

    private fun TaskContainer.dokka(
        inlineSamples: TaskProvider<InlineSamplesTask>
    ): Unit = this.withType<AbstractDokkaLeafTask>().configureEach {
        this.dokkaSourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME).configure {
            val source: Provider<Directory> = inlineSamples.flatMap(
                InlineSamplesTask::outputDirectory
            )
            this.sourceRoots.setFrom(source)
        }
    }
}
