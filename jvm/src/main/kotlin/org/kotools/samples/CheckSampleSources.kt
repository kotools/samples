package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.kotools.samples.internal.isClassDeclaration
import org.kotools.samples.internal.isJava
import org.kotools.samples.internal.isKotlin
import org.kotools.samples.internal.isPublicClassDeclarationInJava
import org.kotools.samples.internal.isPublicClassDeclarationInKotlin
import org.kotools.samples.internal.isSample
import java.io.File

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
    internal fun execute() {
        val noErrorFound: Boolean = this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .filter(File::isSample)
            .filter { it.isKotlin() || it.isJava() }
            .toSet()
            .map { it.hasSinglePublicClass(logger::error) }
            .all { it }
        if (noErrorFound) return
        error("Found some errors while checking sample source files.")
    }
}

private fun File.hasSinglePublicClass(log: (String) -> Unit): Boolean {
    val classDeclarations: List<String> = this.useLines {
        it.map(String::trim)
            .filter(String::isClassDeclaration)
            .toList()
    }
    if (classDeclarations.count() > 1) {
        log("Multiple classes found in '$this'.")
        return false
    }
    val publicClassCount: Int = classDeclarations.count {
        if (this.isKotlin()) it.isPublicClassDeclarationInKotlin()
        else it.isPublicClassDeclarationInJava()
    }
    if (publicClassCount == 1) return true
    log("No public class found in '$this'.")
    return false
}
