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
import org.kotools.samples.internal.sampleNotFound
import java.io.File

/** Gradle task responsible for checking sample references from main sources. */
@DisableCachingByDefault(because = "Only reading files doesn't worth caching.")
public abstract class CheckSampleReferences : DefaultTask() {
    /**
     * The directory containing main sources to check that may reference files
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
        val samplesDirectory: Directory = this.extractedSamplesDirectory.get()
        val sampleNotFound: Boolean = this.sourceDirectory.asFileTree
            .asSequence()
            .filterNotNull()
            .filter(File::isKotlin)
            .flatMap { it.sampleIdentifiers() }
            .filterNot { it.sampleExistsIn(samplesDirectory) }
            .map(String::sampleNotFound)
            .onEach(this.logger::error)
            .toSet()
            .any()
        if (!sampleNotFound) return
        error("Errors found while checking sample references.")
    }
}

private fun File.sampleIdentifiers(): Set<String> = this.useLines {
    it.filter(String::isSampleReference)
        .map(String::sampleIdentifier)
        .toSet()
}

private fun String.sampleExistsIn(directory: Directory): Boolean {
    val path: String = this.sampleIdentifierToSamplePath()
    return directory.file(path)
        .asFile
        .exists()
}
