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
        val numberOfClasses: Int = this.countClasses()
        if (numberOfClasses == 1) return
        val message =
            "The following file should have a single class: ${this.file.path}."
        error(message)
    }

    private fun countClasses(): Int =
        this.file.useLines { lines: Sequence<String> ->
            lines.count { this.language.classHeaderRegex in it }
        }

    /** Returns all samples present in this source file. */
    fun samples(): Set<Sample> {
        var identifier: MutableList<String> = mutableListOf()
        val body: MutableList<String> = mutableListOf()
        val samples: MutableList<Sample> = mutableListOf()
        var numberOfUnclosedBracketsInSample = 0
        var readBody = false
        this.file.useLines { lines: Sequence<String> ->
            lines.forEach {
                when {
                    this.language.isPackageDeclaration(it) -> identifier +=
                        it.substringAfter("${this.language.packageKeyword} ")
                            .substringBefore(';')
                            .split('.')
                    this.language.classHeaderRegex in it -> identifier += it
                        .substringAfter("${this.language.classKeyword} ")
                        .substringBefore(" {")
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

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this sample source file. */
    override fun toString(): String =
        "Sample source file at '${this.file.path}'."

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [SampleSourceFile] type. */
    companion object {
        /**
         * Creates an instance of [SampleSourceFile] from the specified [file].
         * Returns `null` if the [file] is not in a test Kotlin source set, if
         * the [file]'s name is not suffixed by `Sample`, or if it is
         * unsupported.
         */
        fun orNull(file: File): SampleSourceFile? {
            val language: ProgrammingLanguage = file
                .takeIf { it.path.contains("test/", ignoreCase = true) }
                ?.takeIf { it.nameWithoutExtension.endsWith("Sample") }
                ?.let(ProgrammingLanguage.Companion::orNull)
                ?: return null
            return SampleSourceFile(file, language)
        }
    }
}
