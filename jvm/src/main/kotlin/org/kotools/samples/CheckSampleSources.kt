package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.SampleSourceFile

/**
 * Represents a Gradle task that checks the content of the sample source files
 * present in the specified [source directory][sourceDirectory].
 *
 * A sample source file is a Kotlin or Java file that has a name suffixed by
 * `Sample`.
 *
 * This task throws an [IllegalStateException] if a sample source file has an
 * invalid content. For example, the sample source file's content is invalid if
 * it doesn't contain a single public class declaration.
 */
@DisableCachingByDefault(because = "Only reading files doesn't worth caching.")
public abstract class CheckSampleSources : DefaultTask() {
    /** The directory containing the sample source files to check. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    @TaskAction
    internal fun execute(): Unit = this.sourceDirectory.asFileTree.asSequence()
        .filterNotNull()
        .mapNotNull(SampleSourceFile.Companion::orNull)
        .forEach(SampleSourceFile::checkSingleClass)
}
