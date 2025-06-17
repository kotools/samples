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
     * Checks that this sample source file contains a single public class, and
     * throws an [IllegalStateException] if this file contains multiple classes
     * or no public class at all.
     */
    fun checkSinglePublicClass() {
        val classDeclarations: List<String> = this.file.useLines {
            it.map(String::trim)
                .filter(this.language::isClassDeclaration)
                .toList()
        }
        if (classDeclarations.count() > 1) {
            val message: ExceptionMessage =
                ExceptionMessage.multipleClassesFoundIn(this.file)
            error(message)
        }
        val publicClassCount: Int =
            classDeclarations.count(this.language::isPublicClassDeclaration)
        if (publicClassCount == 0) {
            val message: ExceptionMessage =
                ExceptionMessage.noPublicClassFoundIn(this.file)
            error(message)
        }
    }

    /**
     * Checks that this sample source file doesn't contain a single-expression
     * [Kotlin] function, or throws an [IllegalStateException] if this is the
     * case. Does nothing if this sample source is a [Java] file.
     *
     * See the [single-expression Kotlin functions](https://kotlinlang.org/docs/functions.html#single-expression-functions)
     * documentation for more details about their syntax.
     */
    fun checkNoSingleExpressionKotlinFunction() {
        val kotlin: Kotlin = this.language as? Kotlin
            ?: return
        val fileHasSingleExpressionFunction: Boolean = this.file.useLines {
            it.map(String::trim)
                .any(kotlin::isSingleExpressionFunction)
        }
        if (!fileHasSingleExpressionFunction) return
        val message: ExceptionMessage =
            ExceptionMessage.singleExpressionKotlinFunctionFoundIn(this.file)
        error(message)
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
        .useLines { it.firstOrNull(this.language::isPackageDeclaration) }
        ?.substringAfter("${this.language.packageKeyword} ")
        ?.substringBefore(';')

    private fun className(): String = this.file.useLines {
        it.first(this.language::isPublicClassDeclaration)
            .let(this.language::classNameOrNull)
            ?: error("Class name not found in '$this'.")
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
