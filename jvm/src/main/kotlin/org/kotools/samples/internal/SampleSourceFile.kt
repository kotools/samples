package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source file.
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [SampleSourceFile.Companion] type.
 */
internal class SampleSourceFile private constructor(
    private val file: File,
    private val language: ProgrammingLanguage
) {
    // ----------------------- File content's operations -----------------------

    /**
     * Checks that this sample source file contains a single class, and throws
     * an [IllegalStateException] if this is not the case.
     */
    fun checkSingleClass() {
        val classCount: Int = this.file.useLines { lines: Sequence<String> ->
            lines.map(String::trim)
                .count(this.language::isPublicClassDeclaration)
        }
        if (classCount == 0) error("No public class found in '${this.file}'.")
        if (classCount > 1)
            error("Multiple public classes found in '${this.file}'.")
    }

    /** Returns all samples present in this source file. */
    fun samples(): Set<Sample> {
        var identifier: MutableList<String> = this.packageIdentifierOrNull()
            ?.split('.')
            ?.toMutableList()
            ?: mutableListOf()
        identifier += this.className()
        val body: MutableList<String> = mutableListOf()
        val samples: MutableList<Sample> = mutableListOf()
        var numberOfUnclosedBracketsInSample = 0
        var readBody = false
        this.file.useLines { lines: Sequence<String> ->
            lines.forEach {
                when {
                    this.language.functionHeaderRegex in it -> {
                        identifier += it.substringBefore('(')
                            .substringAfter("${this.language.functionKeyword} ")
                        numberOfUnclosedBracketsInSample++
                        readBody = true
                    }
                    readBody -> {
                        if ('{' in it) numberOfUnclosedBracketsInSample++
                        if ('}' in it) numberOfUnclosedBracketsInSample--
                        if (numberOfUnclosedBracketsInSample > 0) body += it
                        else {
                            readBody = false
                            samples += Sample(
                                identifier.toList(),
                                body.toList(),
                                this.language
                            )
                            identifier = identifier.dropLast(1)
                                .toMutableList()
                            body.clear()
                        }
                    }
                }
            }
        }
        return samples.toSet()
    }

    /**
     * Returns the package identifier specified in this sample source file, or
     * returns `null` if no package identifier was found.
     */
    fun packageIdentifierOrNull(): String? = this.file
        .useLines { it.firstOrNull(this.language.packageRegex::matches) }
        ?.substringAfter("${this.language.packageKeyword} ")
        ?.substringBefore(';')

    /**
     * Returns the name of the public class contained in this sample source
     * file.
     */
    fun className(): String = this.file.useLines {
        it.first(this.language::isPublicClassDeclaration)
            .substringAfter("${this.language.classKeyword} ")
            .substringBefore(" {")
    }

    // ------------------------------ Conversions ------------------------------

    /** Returns the path of this sample source file. */
    override fun toString(): String = this.file.toString()

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [SampleSourceFile] type. */
    companion object {
        /**
         * Returns the specified [file] as a sample source file, or returns
         * `null` if the [file] is unsupported or if its name is not suffixed by
         * `Sample`.
         */
        fun orNull(file: File): SampleSourceFile? = file
            .takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.let(ProgrammingLanguage.Companion::orNull)
            ?.let { SampleSourceFile(file, language = it) }
    }
}
