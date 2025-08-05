package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.KotlinSampleSource
import org.kotools.samples.internal.isClass
import org.kotools.samples.internal.isJava
import org.kotools.samples.internal.isJavaPublicClass
import org.kotools.samples.internal.isSample
import org.kotools.samples.internal.multipleClassesFound
import org.kotools.samples.internal.noPublicClassFound
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
            .mapNotNull(KotlinSampleSource.Companion::orNull)
            .mapNotNull(KotlinSampleSource::contentError)
            .onEach { this.logger.error(it.message) }
            .toSet()
            .any()
        val javaErrorFound: Boolean = files.filter(File::isSample)
            .filter(File::isJava)
            .mapNotNull(File::findJavaContentError)
            .mapNotNull(IllegalStateException::message)
            .onEach(this.logger::error)
            .toSet()
            .any()
        if (!kotlinErrorFound && !javaErrorFound) return
        error("Errors found while checking the content of sample sources.")
    }
}

// ------------------------------- Kotlin & Java -------------------------------

private fun File.classes(): List<String> = this.useLines {
    it.map(String::trim)
        .filter(String::isClass)
        .toList()
}
// ----------------------------------- Java ------------------------------------

private fun File.findJavaContentError(): IllegalStateException? {
    val classes: List<String> = this.classes()
    if (classes.count() > 1) return this.multipleClassesFound()
    val publicClassCount: Int = classes.count(String::isJavaPublicClass)
    return if (publicClassCount == 0) this.noPublicClassFound()
    else null
}
