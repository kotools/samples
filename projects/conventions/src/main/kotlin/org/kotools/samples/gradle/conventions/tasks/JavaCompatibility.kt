package org.kotools.samples.gradle.conventions.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile
import org.kotools.samples.gradle.conventions.CompatibilityPluginExtension

/** Task that detects the compatibility of a project with Java. */
@CacheableTask
public abstract class JavaCompatibility internal constructor() : DefaultTask() {
    /**
     * Java version to be compatible with, usually set with
     * [CompatibilityPluginExtension.java].
     *
     * If this property is not set, this task prints Java
     * [source][sourceVersion] and [target][targetVersion] compatibilities.
     */
    @get:Input
    @get:Optional
    public abstract val compatibilityVersion: Property<String>

    /**
     * Default Java source compatibility, usually set with
     * [JavaCompile.getSourceCompatibility].
     */
    @get:Input
    public abstract val sourceVersion: Property<String>

    /**
     * Default Java target compatibility, usually set with
     * [JavaCompile.getTargetCompatibility].
     */
    @get:Input
    public abstract val targetVersion: Property<String>

    /** File that will contain the detected Java compatibility. */
    @get:OutputFile
    public abstract val outputFile: RegularFileProperty

    @TaskAction
    internal fun execute() {
        val compatibility: String = this.compatibility()
        this.outputFile.get()
            .asFile
            .writeText(compatibility)
    }

    private fun compatibility(): String {
        val compatibilityVersion: String? = this.compatibilityVersion.orNull
        if (compatibilityVersion != null) return "Java $compatibilityVersion"
        val sourceVersion: String = this.sourceVersion.get()
        val targetVersion: String = this.targetVersion.get()
        if (sourceVersion == targetVersion) return "Java $sourceVersion"
        return "Java $sourceVersion (target: $targetVersion)"
    }
}
