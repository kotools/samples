package org.kotools.samples

import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

// ----------------------------- Script properties -----------------------------

private val srcDirectory: Directory = layout.projectDirectory.dir("src")

private val kotoolsSamplesDirectory: Provider<Directory> =
    layout.buildDirectory.dir("kotools-samples")

private val sourcesBackupDirectory: Provider<Directory> =
    kotoolsSamplesDirectory.map { it.dir("sources-backup") }

// ----------------------------------- Tasks -----------------------------------

private val checkSampleSources: TaskProvider<CheckSampleSources> by tasks
    .registering(CheckSampleSources::class) {
        this.sourceDirectory = srcDirectory.dir("test")
    }

private val extractSamples: TaskProvider<ExtractSamples> by tasks.registering(
    ExtractSamples::class
) {
    this.sourceDirectory = srcDirectory.dir("test")
    this.outputDirectory = kotoolsSamplesDirectory.map { it.dir("extracted") }
    this.dependsOn(checkSampleSources)
}

private val checkSampleReferences: TaskProvider<CheckSampleReferences> by tasks
    .registering(CheckSampleReferences::class) {
        this.sourceDirectory = srcDirectory.dir("main")
        this.extractedSamplesDirectory =
            extractSamples.flatMap(ExtractSamples::outputDirectory)
    }

private val cleanMainSourcesBackup: TaskProvider<Delete> by tasks
    .registering(Delete::class) { this.setDelete(sourcesBackupDirectory) }

private val backupMainSources: TaskProvider<Copy> by tasks.registering(
    Copy::class
) {
    this.from(srcDirectory) { exclude("api", "sample", "test") }
    this.into(sourcesBackupDirectory)
    this.dependsOn(checkSampleReferences, cleanMainSourcesBackup)
}

private val inlineSamples: TaskProvider<InlineSamples> by tasks.registering(
    InlineSamples::class
) {
    this.sourceDirectory = srcDirectory.dir("main")
    this.extractedSamplesDirectory =
        extractSamples.flatMap(ExtractSamples::outputDirectory)
    this.dependsOn(backupMainSources)
}

private val restoreMainSources: TaskProvider<Copy> by tasks.registering(
    Copy::class
) {
    this.from(sourcesBackupDirectory)
    this.into(srcDirectory)
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
