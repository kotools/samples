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
                            val sampleIdentifier: String =
                                identifier.joinToString(separator = ".")
                            val sampleBody: String = body
                                .joinToString(separator = "\n")
                                .trimIndent()
                            samples += Sample(
                                sampleIdentifier,
                                sampleBody,
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

    private fun packageIdentifierOrNull(): String? = this.file
        .useLines { it.firstOrNull(this.language.packageRegex::matches) }
        ?.substringAfter("${this.language.packageKeyword} ")
        ?.substringBefore(';')

    private fun className(): String = this.file.useLines {
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
