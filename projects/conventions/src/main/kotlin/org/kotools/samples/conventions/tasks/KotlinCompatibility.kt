package org.kotools.samples.conventions.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.kotools.samples.conventions.CompatibilityPluginExtension

/** Task that prints Kotlin compatibility of a project. */
@DisableCachingByDefault(because = "Prints to standard output.")
public abstract class KotlinCompatibility internal constructor() :
    DefaultTask() {
    /**
     * Kotlin version to be compatible with, usually set with
     * [CompatibilityPluginExtension.kotlin].
     *
     * If this property is not set, this task prints Kotlin [API][apiVersion],
     * [Language][languageVersion] and [Core Libraries][coreLibrariesVersion]
     * compatibilities.
     */
    @get:Input
    @get:Optional
    public abstract val compatibilityVersion: Property<String>

    /**
     * Default Kotlin API version, usually set with
     * [KotlinJvmCompilerOptions.apiVersion].
     */
    @get:Input
    public abstract val apiVersion: Property<KotlinVersion>

    /**
     * Default Kotlin Language version, usually set with
     * [KotlinJvmCompilerOptions.languageVersion].
     */
    @get:Input
    public abstract val languageVersion: Property<KotlinVersion>

    /**
     * Default Kotlin Core Libraries version, usually set with
     * [KotlinJvmProjectExtension.coreLibrariesVersion].
     */
    @get:Input
    public abstract val coreLibrariesVersion: Property<String>

    @TaskAction
    internal fun execute() {
        val version: String = this.compatibilityVersion.orNull
            ?: this.coreLibrariesVersion.get()
        val apiVersion: String = this.apiVersion.get().version
        val languageVersion: String = this.languageVersion.get().version
        println(
            "Kotlin $version (api: $apiVersion, language: $languageVersion)"
        )
    }
}
