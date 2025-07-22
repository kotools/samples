package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.isKotlin
import org.kotools.samples.internal.isSampleReference
import org.kotools.samples.internal.sampleIdentifier
import org.kotools.samples.internal.sampleIdentifierToSamplePath
import org.kotools.samples.internal.sampleReferenceKeyword
import java.io.File

/**
 * Gradle task responsible for inlining samples referenced from main sources.
 */
@DisableCachingByDefault(because = "Writes directly in files to read.")
public abstract class InlineSamples : DefaultTask() {
    /**
     * The directory containing main sources to read that may reference files
     * within the [extractedSamplesDirectory].
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /**
     * The directory containing extracted samples that may be referenced from
     * files within the [sourceDirectory].
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val extractedSamplesDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        val files: Sequence<File> = this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .filter(File::isKotlin)
        val samplesDirectory: Directory = this.extractedSamplesDirectory.get()
        val samples: Map<String, List<String>> = files
            .flatMap(File::sampleIdentifiers)
            .associateWith(samplesDirectory::sampleLines)
        files.forEach {
            val content: String = it.readLines()
                .joinToString("\n", transform = samples::sampleTextOrOriginal)
            it.writeText("$content\n")
        }
    }
}

private fun File.sampleIdentifiers(): Set<String> = this.useLines {
    it.filter(String::isSampleReference)
        .map(String::sampleIdentifier)
        .toSet()
}

private fun Directory.sampleLines(identifier: String): List<String> = identifier
    .sampleIdentifierToSamplePath()
    .let(this::file)
    .asFile
    .readLines()

private fun Map<String, List<String>>.sampleTextOrOriginal(
    line: String
): String {
    if (!line.isSampleReference()) return line
    val keyword: String = sampleReferenceKeyword()
    val prefix: String = line.substringBefore(keyword)
    val sampleIdentifier: String = line.sampleIdentifier()
    return this[sampleIdentifier]
        ?.joinToString(separator = "\n") { "$prefix$it" }
        ?: line
}
