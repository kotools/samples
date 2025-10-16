package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.core.KotlinSampleSource
import org.kotools.samples.internal.JavaSampleSource
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
        val kotlinErrorFound: Boolean = files
            .filter(KotlinSampleSource::isValid)
            .associateWith(File::kotlinClassesAndFunctions)
            .mapNotNull {
                KotlinSampleSource.classFunctionError(it.key, it.value)
            }
            .onEach(this.logger::error)
            .any()
        val javaErrorFound: Boolean = files
            .mapNotNull(JavaSampleSource.Companion::orNull)
            .mapNotNull(JavaSampleSource::contentError)
            .onEach { this.logger.error(it.message) }
            .toSet()
            .any()
        if (kotlinErrorFound || javaErrorFound)
            error("Errors found while checking the content of sample sources.")
    }
}

private fun File.kotlinClassesAndFunctions(): List<String> = this.useLines {
    it.filter(KotlinSampleSource::isClassOrFunction)
        .map(String::trim)
        .toList()
}
