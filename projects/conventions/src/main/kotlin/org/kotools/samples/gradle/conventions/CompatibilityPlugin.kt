package org.kotools.samples.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.kotools.samples.gradle.conventions.tasks.Compatibilities
import org.kotools.samples.gradle.conventions.tasks.JavaCompatibility
import org.kotools.samples.gradle.conventions.tasks.KotlinCompatibility

/** Project plugin that configures Java and Kotlin compatibilities. */
public class CompatibilityPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        val compatibility: CompatibilityPluginExtension =
            project.extensions.create("compatibility")
        project.withKotlinJvmPlugin(compatibility)
        project.tasks.javaCompile(compatibility)
    }
}

// -------------------------------- Kotlin/JVM ---------------------------------

private fun Project.withKotlinJvmPlugin(
    compatibility: CompatibilityPluginExtension
): Unit = this.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    project.extensions.kotlinJvm(compatibility)

    val javaCompatibility: TaskProvider<JavaCompatibility> =
        project.tasks.javaCompatibility(compatibility)
    val kotlinCompatibility: TaskProvider<KotlinCompatibility> =
        project.tasks.kotlinCompatibility(compatibility)
    project.tasks.compatibilities(javaCompatibility, kotlinCompatibility)
}

private fun ExtensionContainer.kotlinJvm(
    compatibility: CompatibilityPluginExtension
) {
    val kotlin: KotlinJvmProjectExtension = this.getByType()

    val kotlinVersion: Provider<KotlinVersion> = compatibility.kotlin
        .map { it.substringBeforeLast('.') }
        .map(KotlinVersion.Companion::fromVersion)
    kotlin.compilerOptions.apiVersion.set(kotlinVersion)
    kotlin.compilerOptions.languageVersion.set(kotlinVersion)

    compatibility.kotlin.map { kotlin.coreLibrariesVersion = it }

    val jvmTarget: Provider<JvmTarget> =
        compatibility.java.map(JvmTarget.Companion::fromTarget)
    kotlin.compilerOptions.jvmTarget.set(jvmTarget)

    val jdkRelease: Provider<String> =
        compatibility.java.map { "-Xjdk-release=$it" }
    kotlin.compilerOptions.freeCompilerArgs.add(jdkRelease)
}

private fun TaskContainer.javaCompatibility(
    compatibility: CompatibilityPluginExtension
): TaskProvider<JavaCompatibility> {
    val task: TaskProvider<JavaCompatibility> =
        this.register<JavaCompatibility>("javaCompatibility")
    task.configure {
        this.description = "Prints Java compatibility."
        this.group = "compatibility"

        this.compatibilityVersion.set(compatibility.java)
        val compileJava: TaskProvider<JavaCompile> =
            this.project.tasks.named<JavaCompile>("compileJava")
        val source: Provider<String> =
            compileJava.map { it.sourceCompatibility }
        this.sourceVersion.set(source)
        val target: Provider<String> =
            compileJava.map { it.targetCompatibility }
        this.targetVersion.set(target)

        val outputFile: Provider<RegularFile> =
            this.project.layout.buildDirectory.file("compatibility/java.txt")
        this.outputFile.set(outputFile)
    }
    return task
}

private fun TaskContainer.kotlinCompatibility(
    compatibility: CompatibilityPluginExtension
): TaskProvider<KotlinCompatibility> {
    val task: TaskProvider<KotlinCompatibility> =
        this.register<KotlinCompatibility>("kotlinCompatibility")
    task.configure {
        this.description = "Prints Kotlin compatibility."
        this.group = "compatibility"

        this.compatibilityVersion.set(compatibility.kotlin)
        val kotlin: KotlinJvmProjectExtension =
            this.project.extensions.getByType()
        this.apiVersion.set(kotlin.compilerOptions.apiVersion)
        this.languageVersion.set(kotlin.compilerOptions.languageVersion)
        this.coreLibrariesVersion.set(kotlin.coreLibrariesVersion)

        val outputFile: Provider<RegularFile> =
            this.project.layout.buildDirectory.file("compatibility/kotlin.txt")
        this.outputFile.set(outputFile)
    }
    return task
}

private fun TaskContainer.compatibilities(
    javaCompatibility: TaskProvider<JavaCompatibility>,
    kotlinCompatibility: TaskProvider<KotlinCompatibility>
): Unit = this.register<Compatibilities>("compatibilities").configure {
    this.description = "Prints detected compatibilities."
    this.group = "help"

    val javaCompatibilityFile: Provider<RegularFile> =
        javaCompatibility.flatMap(JavaCompatibility::outputFile)
    this.javaCompatibility.set(javaCompatibilityFile)
    val kotlinCompatibilityFile: Provider<RegularFile> =
        kotlinCompatibility.flatMap(KotlinCompatibility::outputFile)
    this.kotlinCompatibility.set(kotlinCompatibilityFile)
}

// ----------------------------------- Java ------------------------------------

private fun TaskContainer.javaCompile(
    compatibility: CompatibilityPluginExtension
): Unit = this.withType<JavaCompile>().configureEach {
    val release: Provider<Int> = compatibility.java.map(String::toInt)
    this.options.release.set(release)
}
