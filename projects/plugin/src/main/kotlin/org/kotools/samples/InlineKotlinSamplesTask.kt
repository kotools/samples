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
 * Task that inlines Kotlin samples referenced from sources.
 *
 * - Inputs: [sourceDirectory] and [extractedSampleDirectory].
 * - Output: [outputDirectory].
 */
@CacheableTask
public abstract class InlineKotlinSamplesTask internal constructor() :
    DefaultTask() {
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
            it.map(this::sampleOrOriginal)
                .joinToString("\n")
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
        return if (keyword in line) {
            val prefix: String = line.substringBefore(keyword)
            val path: String = line.substringAfter('[')
                .substringBefore(']')
                .replace('.', '/')
                .plus(".md")
            this.sampleText(prefix, path)
        } else line
    }

    private fun sampleText(prefix: String, path: String): String = this
        .extractedSampleDirectory
        .file(path)
        .get()
        .asFile
        .useLines { lines: Sequence<String> ->
            lines.joinToString(separator = "\n") { "$prefix$it" }
        }
}
