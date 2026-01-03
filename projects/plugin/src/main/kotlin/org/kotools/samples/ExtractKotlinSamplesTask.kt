package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import org.kotools.samples.core.KotlinFile
import org.kotools.samples.core.KotlinSample
import org.kotools.samples.core.SamplePath
import java.io.File

/**
 * Task that extracts Kotlin samples as Markdown files, from
 * [sources][sourceDirectory] into the [output][outputDirectory] directory.
 */
@CacheableTask
public abstract class ExtractKotlinSamplesTask internal constructor() :
    DefaultTask() {
    /** Directory containing Kotlin samples to extract. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /** Directory that will contain extracted Kotlin samples. */
    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        // TODO: Refactor for using functional programming.
        this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .mapNotNull(KotlinFile.Companion::fromOrNull)
            .flatMap(KotlinFile::samples)
            .forEach(this::save)
    }

    private fun save(sample: KotlinSample) {
        val path: SamplePath = sample.path()
        val content: String = sample.markdownFileContent()
        this.outputDirectory.get()
            .file("$path")
            .asFile
            .also(File::ensureParentDirsCreated)
            .writeText(content)
    }
}
