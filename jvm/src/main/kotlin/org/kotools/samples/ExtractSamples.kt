package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.SampleSourceFile

/**
 * Represents a Gradle task that extracts samples, from the sample source files
 * present in the specified [sourceDirectory], to the specified
 * [outputDirectory].
 *
 * A sample source file is a [Kotlin](https://kotlinlang.org) or
 * [Java](https://www.java.com) file that has a name suffixed by `Sample`.
 * Also, a sample is a Markdown file that contains one code block, written in
 * [Kotlin](https://kotlinlang.org) or in [Java](https://www.java.com), ready to
 * be inlined in the documentation.
 */
@DisableCachingByDefault(because = "Generating files doesn't worth caching.")
public abstract class ExtractSamples : DefaultTask() {
    /**
     * The directory containing the sample source files to extract samples from.
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /** The directory that will contain the extracted samples. */
    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        val directory: Directory = this.outputDirectory.get()
        this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .mapNotNull(SampleSourceFile.Companion::orNull)
            .flatMap(SampleSourceFile::samples)
            .forEach { it.saveFileIn(directory) }
    }
}
