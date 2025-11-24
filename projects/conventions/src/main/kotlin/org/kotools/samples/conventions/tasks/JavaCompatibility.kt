package org.kotools.samples.conventions.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.conventions.CompatibilityExtension

/** Task that prints Java compatibility of a project. */
@DisableCachingByDefault(because = "Prints to standard output.")
public abstract class JavaCompatibility internal constructor() : DefaultTask() {
    /**
     * Java version to be compatible with, usually set with
     * [CompatibilityExtension.java].
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

    @TaskAction
    internal fun execute() {
        val version: String? = this.compatibilityVersion.orNull
        if (version != null) return println("Java $version")
        val sourceVersion: String = this.sourceVersion.get()
        val targetVersion: String = this.targetVersion.get()
        return if (sourceVersion == targetVersion)
            println("Java $sourceVersion")
        else println("Java $sourceVersion (source) and $targetVersion (target)")
    }
}
