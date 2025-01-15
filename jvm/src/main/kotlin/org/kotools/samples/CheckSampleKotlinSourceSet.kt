package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.SampleSourceFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Task responsible for checking the content of the `sample` Kotlin source set.
 * If this source set contains sample source files, this task warns the user
 * with a deprecation notice.
 */
@DisableCachingByDefault(because = "This task only reads files.")
public abstract class CheckSampleKotlinSourceSet : DefaultTask() {
    /** The directory corresponding to the `sample` Kotlin source set. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    @TaskAction
    private fun execute() {
        val sourceSetHasFiles: Boolean = this.sourceDirectory.asFileTree
            .asSequence()
            .filterNotNull()
            .mapNotNull(SampleSourceFile.Companion::orNull)
            .any()
        if (!sourceSetHasFiles) return
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
        val deprecationNotice = "The 'sample' Kotlin source set is " +
                "deprecated. Please move your samples to the 'test' Kotlin " +
                "source set and suffix their file name with 'Sample'."
        logger.warn(deprecationNotice)
    }
}
