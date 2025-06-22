package org.kotools.samples

import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// ----------------------------- Script properties -----------------------------

private val projectSources: Directory = layout.projectDirectory.dir("src")

private val output: Provider<Directory> =
    layout.buildDirectory.dir("kotools-samples")
private val sourcesBackup: Provider<Directory> =
    output.map { it.dir("sources-backup") }

// ----------------------------------- Tasks -----------------------------------

private val checkSampleSources: TaskProvider<CheckSampleSources> by tasks
    .registering(CheckSampleSources::class) {
        this.description = "Checks the content of sample sources."
        this.sourceDirectory = projectSources.dir("test")
    }

private val extractSamples: TaskProvider<ExtractSamples> by tasks.registering(
    ExtractSamples::class
) {
    this.description = "Extracts samples for KDoc."
    this.dependsOn(checkSampleSources)
    this.sourceDirectory = projectSources
    this.outputDirectory = output.map { it.dir("extracted") }
}

private val checkSampleReferences: TaskProvider<CheckSampleReferences> by tasks
    .registering(CheckSampleReferences::class) {
        this.description = "Checks sample references from KDoc."
        this.sourceDirectory = projectSources
        this.extractedSamplesDirectory =
            extractSamples.flatMap(ExtractSamples::outputDirectory)
    }

private val cleanMainSourcesBackup: TaskProvider<Delete> by tasks.registering(
    Delete::class
) {
    this.description = "Deletes main sources backup from the build directory."
    this.setDelete(sourcesBackup)
}

private val backupMainSources: TaskProvider<Copy> by tasks.registering(
    Copy::class
) {
    this.description = "Copies main sources into the build directory."
    this.dependsOn(checkSampleReferences, cleanMainSourcesBackup)
    this.from(projectSources) { exclude("api", "sample", "test") }
    this.into(sourcesBackup)
}

private val inlineSamples: TaskProvider<InlineSamples> by tasks.registering(
    InlineSamples::class
) {
    this.description = "Inlines KDoc samples."
    this.dependsOn(backupMainSources)
    this.sourceDirectory = projectSources
    this.extractedSamplesDirectory =
        extractSamples.flatMap(ExtractSamples::outputDirectory)
}

private val restoreMainSources: TaskProvider<Copy> by tasks.registering(
    Copy::class
) {
    this.description = "Restores main sources backup from the build directory."
    this.from(sourcesBackup)
    this.into(projectSources)
    this.outputs.upToDateWhen { false }
}

// ----------------------------- Dokka integration -----------------------------

tasks.withType<AbstractDokkaLeafTask>().configureEach {
    this.dependsOn(inlineSamples)
    this.finalizedBy(restoreMainSources)
}

rootProject.tasks.withType<DokkaMultiModuleTask>().configureEach {
    this.dependsOn(restoreMainSources)
}

tasks.withType<DokkaGenerateTask>().configureEach {
    // Configuration for Dokka Gradle plugin v2
    this.dependsOn(inlineSamples)
    this.finalizedBy(restoreMainSources)
}

// ---------------------------- Kotlin integration -----------------------------

tasks.named("check").configure { this.dependsOn(checkSampleReferences) }

tasks.named("kotlinSourcesJar").configure {
    this.dependsOn(inlineSamples)
    this.finalizedBy(restoreMainSources)
}

private val kotlinCompilationTasks: TaskCollection<KotlinCompilationTask<*>> =
    tasks.withType(KotlinCompilationTask::class)
restoreMainSources.configure { this.mustRunAfter(kotlinCompilationTasks) }
