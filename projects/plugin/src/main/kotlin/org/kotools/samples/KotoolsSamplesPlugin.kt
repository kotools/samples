package org.kotools.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.core.extra
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.DokkaExtension
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
            val checkKotlinSamples: TaskProvider<CheckKotlinSamplesTask> =
                checkKotlinSamplesTask(project, sampleDirectory)
            val extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask> =
                extractKotlinSamplesTask(project, checkKotlinSamples)
            val checkSampleReferences: TaskProvider<CheckSampleReferencesTask> =
                checkSampleReferencesTask(project, extractKotlinSamples)
            val inlineSamples: TaskProvider<InlineSamplesTask> =
                inlineSamplesTask(project, checkSampleReferences)
            // Integrations:
            configureKotlinJvm(
                project, sampleDirectory, checkSampleReferences, inlineSamples
            )
            configureDokka(project, inlineSamples)
        }
}

// --------------------------------- New tasks ---------------------------------

private fun checkKotlinSamplesTask(
    project: Project,
    sourceDirectory: Directory
): TaskProvider<CheckKotlinSamplesTask> {
    val task: TaskProvider<CheckKotlinSamplesTask> =
        project.tasks.register<CheckKotlinSamplesTask>("checkKotlinSamples")
    task.configure {
        this.description = "Checks the content of Kotlin samples from sources."
        this.group = "Kotools Samples"

        // Inputs:
        this.sourceDirectory.set(sourceDirectory)
    }
    return task
}

private fun extractKotlinSamplesTask(
    project: Project,
    checkKotlinSamples: TaskProvider<CheckKotlinSamplesTask>
): TaskProvider<ExtractKotlinSamplesTask> {
    val task: TaskProvider<ExtractKotlinSamplesTask> =
        project.tasks.register<ExtractKotlinSamplesTask>("extractKotlinSamples")
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

private fun checkSampleReferencesTask(
    project: Project,
    extractKotlinSamples: TaskProvider<ExtractKotlinSamplesTask>
): TaskProvider<CheckSampleReferencesTask> {
    val task: TaskProvider<CheckSampleReferencesTask> = project.tasks
        .register<CheckSampleReferencesTask>("checkSampleReferences")
    task.configure {
        this.description =
            "Checks the existence of samples referenced from sources."
        this.group = "Kotools Samples"

        // Inputs:
        this.project.layout.projectDirectory.dir("src/main/kotlin")
            .let(this.sourceDirectory::set)
        extractKotlinSamples.flatMap(ExtractKotlinSamplesTask::outputDirectory)
            .let(this.extractedSampleDirectory::set)
    }
    return task
}

private fun inlineSamplesTask(
    project: Project,
    checkSampleReferences: TaskProvider<CheckSampleReferencesTask>
): TaskProvider<InlineSamplesTask> {
    val task: TaskProvider<InlineSamplesTask> =
        project.tasks.register<InlineSamplesTask>("inlineSamples")
    task.configure {
        this.description = "Inlines Kotlin samples referenced from sources."
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

// -------------------------- Kotlin/JVM integration ---------------------------

private fun configureKotlinJvm(
    project: Project,
    sampleDirectory: Directory,
    checkSampleReferences: TaskProvider<CheckSampleReferencesTask>,
    inlineSamples: TaskProvider<InlineSamplesTask>
) {
    val kotlin: KotlinJvmProjectExtension =
        configureKotlinJvmExtension(project, sampleDirectory)
    configureCheckTask(project, checkSampleReferences)
    configureSourcesJarTasks(project, kotlin, inlineSamples)
}

private fun configureKotlinJvmExtension(
    project: Project,
    sampleDirectory: Directory
): KotlinJvmProjectExtension {
    val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
    kotlin.sourceSets.named(SourceSet.TEST_SOURCE_SET_NAME)
        .get()
        .kotlin
        .srcDir(sampleDirectory)
    return kotlin
}

private fun configureCheckTask(
    project: Project,
    checkSampleReferences: TaskProvider<CheckSampleReferencesTask>
): Unit = project.tasks.named("check")
    .configure { this.dependsOn(checkSampleReferences) }

private fun configureSourcesJarTasks(
    project: Project,
    kotlin: KotlinJvmProjectExtension,
    inlineSamples: TaskProvider<InlineSamplesTask>
): Unit = project.tasks.withType<Jar>()
    .named { it.endsWith("sourcesJar", ignoreCase = true) }
    .configureEach {
        this.dependsOn(inlineSamples)
        this.doFirst {
            kotlin.sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME)
                .configure {
                    project.layout.buildDirectory.dir("kotools-samples/inlined")
                        .let { this.kotlin.setSrcDirs(listOf(it)) }
                }
        }
    }

// ----------------------------- Dokka integration -----------------------------

private fun configureDokka(
    project: Project,
    inlineSamples: TaskProvider<InlineSamplesTask>
): Unit = project.pluginManager.withPlugin("org.jetbrains.dokka") {
    if (isDokkaV2Enabled(project)) configureDokkaV2(project, inlineSamples)
    else configureDokkaV1(project, inlineSamples)
}

private fun isDokkaV2Enabled(project: Project): Boolean {
    val property = "org.jetbrains.dokka.experimental.gradle.pluginMode"
    val values: List<String> = listOf("V2Enabled", "V2EnabledWithHelpers")
    return project.extra.has(property) && project.extra[property] in values
}

private fun configureDokkaV1(
    project: Project,
    inlineSamples: TaskProvider<InlineSamplesTask>
): Unit = project.tasks.withType<AbstractDokkaLeafTask>().configureEach {
    this.dokkaSourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME)
        .configure {
            val source: Provider<Directory> =
                inlineSamples.flatMap(InlineSamplesTask::outputDirectory)
            this.sourceRoots.setFrom(source)
        }
}

private fun configureDokkaV2(
    project: Project,
    inlineSamples: TaskProvider<InlineSamplesTask>
): Unit = project.extensions.getByType<DokkaExtension>()
    .dokkaSourceSets
    .named(SourceSet.MAIN_SOURCE_SET_NAME)
    .configure {
        val source: Provider<Directory> =
            inlineSamples.flatMap(InlineSamplesTask::outputDirectory)
        this.sourceRoots.setFrom(source)
    }
