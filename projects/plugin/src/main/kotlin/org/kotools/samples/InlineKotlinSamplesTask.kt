package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Task that inlines Kotlin samples referenced from sources.
 *
 * - Inputs: [sourceDirectory] and [extractedSampleDirectory].
 * - Output: [outputDirectory].
 */
@CacheableTask
public abstract class InlineKotlinSamplesTask internal constructor() :
    DefaultTask() {
    /** Directory containing sources referencing Kotlin samples. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /**
     * Directory containing extracted Kotlin samples, usually set from
     * [ExtractKotlinSamplesTask.outputDirectory].
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val extractedSampleDirectory: DirectoryProperty

    /** Directory that will contain sources with inlined Kotlin samples. */
    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @TaskAction
    internal fun execute(): Unit = TODO()
}
