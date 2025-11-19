package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/** Project plugin that configures Java and Kotlin compatibilities. */
public class CompatibilityPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        val compatibility: CompatibilityExtension =
            project.extensions.create("compatibility")
        project.withKotlinJvmPlugin(compatibility)
        project.tasks.javaCompile(compatibility)
    }
}

private fun Project.withKotlinJvmPlugin(
    compatibility: CompatibilityExtension
): Unit = this.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()

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

private fun TaskContainer.javaCompile(
    compatibility: CompatibilityExtension
): Unit = this.withType<JavaCompile>().configureEach {
    val release: Provider<Int> = compatibility.java.map(String::toInt)
    this.options.release.set(release)
}
