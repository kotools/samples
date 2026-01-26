package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileNotFoundException

/**
 * Task that checks the existence of [samples][extractedSampleDirectory]
 * referenced from [sources][sourceDirectory].
 */
@CacheableTask
public abstract class CheckSampleReferencesTask internal constructor() :
    DefaultTask() {
    /** Directory containing original sources referencing samples. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /**
     * Directory containing extracted samples, usually set from
     * [ExtractKotlinSamplesTask.outputDirectory].
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val extractedSampleDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        val extractedSamplesDir: Directory = this.extractedSampleDirectory.get()
        this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .filter { it.extension == "kt" }
            .flatMap(File::sampleIdentifiers)
            .forEach(extractedSamplesDir::checkSampleExistence)
    }
}

private fun File.sampleIdentifiers(): Set<String> =
    this.useLines { lines: Sequence<String> ->
        lines.filter { "SAMPLE: [" in it }
            .map { it.substringAfter('[') }
            .map { it.substringBefore(']') }
            .toSet()
    }

private fun Directory.checkSampleExistence(identifier: String) {
    val path: String = identifier.replace(oldChar = '.', newChar = '/')
        .plus(".md")
    val file: File = this.file(path).asFile
    if (!file.exists())
        throw FileNotFoundException("'$identifier' sample not found.")
}
