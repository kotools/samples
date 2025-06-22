package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.ExceptionMessage
import org.kotools.samples.internal.JavaSampleSource
import org.kotools.samples.internal.KotlinSampleSource
import java.io.File

/**
 * Represents a Gradle task that checks the content of the sample sources
 * present in the specified [source directory][sourceDirectory].
 *
 * A sample source is a Kotlin or Java file that has a name suffixed by
 * `Sample`.
 *
 * This task logs all errors encountered in sample sources, then throws an
 * [IllegalStateException] if any error was found.
 */
@DisableCachingByDefault(because = "Only reading files doesn't worth caching.")
public abstract class CheckSampleSources : DefaultTask() {
    /** The directory containing the sample sources to check. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        val files: Sequence<File> = this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
        val errorFoundInKotlinSampleSources: Boolean = files
            .mapNotNull(KotlinSampleSource.Companion::orNull)
            .mapNotNull(KotlinSampleSource::contentExceptionOrNull)
            .map(ExceptionMessage::toString)
            .onEach(logger::error)
            .toSet()
            .any()
        val errorFoundInJavaSampleSources: Boolean = files
            .mapNotNull(JavaSampleSource.Companion::orNull)
            .mapNotNull(JavaSampleSource::contentExceptionOrNull)
            .map(ExceptionMessage::toString)
            .onEach(logger::error)
            .toSet()
            .any()
        if (!errorFoundInKotlinSampleSources && !errorFoundInJavaSampleSources)
            return
        error("Errors found while checking the content of sample sources.")
    }
}
