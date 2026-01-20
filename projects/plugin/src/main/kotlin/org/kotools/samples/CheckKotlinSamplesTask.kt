package org.kotools.samples

import kotlinx.ast.common.klass.KlassDeclaration
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.kotools.samples.internal.parseNodes
import java.io.File

/**
 * Task that checks the content of Kotlin samples from
 * [sources][sourceDirectory].
 */
@CacheableTask
public abstract class CheckKotlinSamplesTask internal constructor() :
    DefaultTask() {
    /** Directory containing Kotlin samples to extract. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    @TaskAction
    internal fun execute(): Unit = this.sourceDirectory.asFileTree.asSequence()
        .filterNotNull()
        .filter { it.extension == "kt" }
        .forEach(File::checkAbsenceOfTopLevelFunction)
}

private fun File.checkAbsenceOfTopLevelFunction() {
    val topLevelFunctionFound: Boolean = this.parseNodes()
        .filterIsInstance<KlassDeclaration>()
        .any { it.keyword == "fun" }
    if (topLevelFunctionFound) throw FileSystemException(
        file = this,
        reason = "Top-level function found in Kotlin sample source."
    )
}
