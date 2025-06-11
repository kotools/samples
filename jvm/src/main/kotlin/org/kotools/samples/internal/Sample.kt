package org.kotools.samples.internal

import org.gradle.api.file.Directory
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File

internal class Sample(
    val identifier: String,
    val body: String,
    val language: ProgrammingLanguage
) {
    fun saveFileIn(directory: Directory) {
        val text: String = listOf(
            "```${this.language.markdownIdentifier}",
            this.body,
            "```"
        ).joinToString(separator = "\n")
        val path: String =
            this.identifier.replace(oldChar = '.', newChar = '/') + ".md"
        directory.file(path)
            .asFile
            .also(File::ensureParentDirsCreated)
            .writeText(text)
    }
}
