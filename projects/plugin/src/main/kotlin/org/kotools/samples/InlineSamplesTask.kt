package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File

/**
 * Task that inlines samples referenced from sources.
 *
 * - Inputs: [sourceDirectory] and [extractedSampleDirectory].
 * - Output: [outputDirectory].
 */
@CacheableTask
public abstract class InlineSamplesTask internal constructor() : DefaultTask() {
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
    internal fun execute(): Unit = this.sourceDirectory.asFileTree.asSequence()
        .filterNotNull()
        .filter { it.name.endsWith(".kt") }
        .forEach(this::inlineSamples)

    private fun inlineSamples(file: File) {
        val content: String = file.useLines {
            it.joinToString(
                separator = "\n",
                transform = this::sampleOrOriginal
            )
        }
        val relativePath: Provider<String> = this.sourceDirectory.map {
            file.path.removePrefix("${it.asFile.path}/")
        }
        this.outputDirectory.file(relativePath)
            .get()
            .asFile
            .also(File::ensureParentDirsCreated)
            .writeText(content)
    }

    private fun sampleOrOriginal(line: String): String {
        val keyword = "SAMPLE: ["
        if (keyword !in line) return line
        val identifier: String = line.substringAfter('[')
            .substringBefore(']')
        if ("/** " in line && " */" in line) {
            val leadingWhitespaces: String = line.substringBefore("/**")
            val sample: String = this.readSample(identifier) { " * $it" }
            return listOf("/**", sample, " */")
                .joinToString(separator = "\n") { "$leadingWhitespaces$it" }
        }
        val prefix: String = line.substringBefore(keyword)
        return this.readSample(identifier) { "$prefix$it" }
    }

    private fun readSample(
        identifier: String,
        transform: (String) -> String
    ): String {
        val path: String = identifier.replace(oldChar = '.', newChar = '/')
            .plus(".md")
        val file: File = this.extractedSampleDirectory.file(path)
            .get()
            .asFile
        return file.useLines {
            it.joinToString(separator = "\n", transform = transform)
        }
    }
}
