package org.kotools.samples

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.kotools.samples.internal.KotlinJvmPluginNotFound
import java.util.Objects

/**
 * Gradle plugin responsible for integrating tested samples in the API
 * reference of Kotlin/JVM projects.
 *
 * @constructor Creates an instance of this plugin.
 */
public class KotoolsSamplesJvmPlugin : Plugin<Project> {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of
     * [KotoolsSamplesJvmPlugin], or returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean =
        other is KotoolsSamplesJvmPlugin

    /** Returns a hash code value for this plugin. */
    override fun hashCode(): Int = Objects.hash("$this")

    // ------------------------- Project configuration -------------------------

    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        this.kotlinSourceSets(project)
        this.javaSourceSets(project)
        val checkSampleSources: TaskProvider<CheckSampleSources> =
            this.checkSampleSourcesTask(project)
        val extractSamples: TaskProvider<ExtractSamples> =
            this.extractSamplesTask(project, checkSampleSources)
        val checkSampleReferences: TaskProvider<CheckSampleReferences> =
            this.checkSampleReferencesTask(project, extractSamples)
        val backupMainSources: TaskProvider<Copy> =
            this.backupMainSourcesTask(project, checkSampleReferences)
        this.inlineSamplesTask(project, backupMainSources, extractSamples)
        this.restoreMainSourcesTask(project)
    }

    private fun kotlinSourceSets(project: Project) {
        val kotlin: KotlinJvmProjectExtension? = project.extensions.findByType()
        checkNotNull(kotlin) { KotlinJvmPluginNotFound(project) }
        val main: KotlinSourceSet = kotlin.sourceSets.getByName("main")
        val sample: KotlinSourceSet = kotlin.sourceSets.create("sample")
        val test: KotlinSourceSet = kotlin.sourceSets.getByName("test")
        sample.dependsOn(main)
        val javaSampleDirectory: Directory = this.javaSampleDirectory(project)
        sample.kotlin.srcDir(javaSampleDirectory)
        test.dependsOn(sample)
    }

    private fun javaSourceSets(project: Project) {
        val javaSampleDirectory: Directory = this.javaSampleDirectory(project)
        project.extensions.getByType<JavaPluginExtension>()
            .sourceSets
            .named("test")
            .configure { this.java.srcDir(javaSampleDirectory) }
    }

    private fun javaSampleDirectory(project: Project): Directory = this
        .sourceDirectory(project)
        .dir("sample/java")

    private fun checkSampleSourcesTask(
        project: Project
    ): TaskProvider<CheckSampleSources> {
        val sourceDirectory: Directory = this.sourceDirectory(project)
        return project.tasks.register<CheckSampleSources>(
            "checkSampleSources"
        ) {
            this.description = "Checks the content of sample sources."
            this.sourceDirectory.set(sourceDirectory)
        }
    }

    private fun extractSamplesTask(
        project: Project,
        checkSampleSources: TaskProvider<CheckSampleSources>
    ): TaskProvider<ExtractSamples> {
        val sourceDirectory: Directory = this.sourceDirectory(project)
        val outputDirectory: Provider<Directory> =
            project.layout.buildDirectory.dir("samples/extracted")
        return project.tasks.register<ExtractSamples>("extractSamples") {
            this.description = "Extracts samples for KDoc."
            this.dependsOn += checkSampleSources
            this.sourceDirectory.set(sourceDirectory)
            this.outputDirectory.set(outputDirectory)
        }
    }

    private fun checkSampleReferencesTask(
        project: Project,
        extractSamples: TaskProvider<ExtractSamples>
    ): TaskProvider<CheckSampleReferences> {
        val sourceDirectory: Directory = this.sourceDirectory(project)
        val extractedSamplesDirectory: Provider<Directory> =
            extractSamples.flatMap(ExtractSamples::outputDirectory)
        return project.tasks
            .register<CheckSampleReferences>("checkSampleReferences") {
                this.description = "Checks sample references from KDoc."
                this.dependsOn += extractSamples
                this.sourceDirectory.set(sourceDirectory)
                this.extractedSamplesDirectory.set(extractedSamplesDirectory)
            }
    }

    private fun backupMainSourcesTask(
        project: Project,
        checkSampleReferences: TaskProvider<CheckSampleReferences>
    ): TaskProvider<Copy> {
        val source: Directory = this.sourceDirectory(project)
        val destination: Provider<Directory> =
            project.layout.buildDirectory.dir("samples/sources-backup")
        return project.tasks.register<Copy>("backupMainSources") {
            this.description =
                "Copies main sources into the build directory."
            this.dependsOn(checkSampleReferences)
            this.from(source) { exclude("api", "sample", "test") }
            this.into(destination)
        }
    }

    private fun inlineSamplesTask(
        project: Project,
        backupMainSources: TaskProvider<Copy>,
        extractSamples: TaskProvider<ExtractSamples>
    ) {
        val source: Directory = this.sourceDirectory(project)
        val extractedSamples: Provider<Directory> =
            extractSamples.flatMap(ExtractSamples::outputDirectory)
        project.tasks.register<InlineSamples>("inlineSamples")
            .configure {
                this.description = "Inlines KDoc samples."
                this.dependsOn += backupMainSources
                this.sourceDirectory.set(source)
                this.extractedSamplesDirectory.set(extractedSamples)
            }
    }

    private fun restoreMainSourcesTask(project: Project) {
        val source: Provider<Directory> =
            project.layout.buildDirectory.dir("samples/sources-backup")
        val destination: Directory = this.sourceDirectory(project)
        project.tasks.register<Copy>("restoreMainSources")
            .configure {
                this.description =
                    "Restores main sources backup from the build directory."
                this.from(source)
                this.into(destination)
                this.outputs.upToDateWhen { false }
            }
    }

    private fun sourceDirectory(project: Project): Directory =
        project.layout.projectDirectory.dir("src")

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this plugin. */
    override fun toString(): String =
        "Kotools Samples Gradle plugin for Kotlin/JVM projects"
}
