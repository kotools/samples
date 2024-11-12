package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source file.
 *
 * See the [SampleSourceFile.Companion.orNull] method for creating an instance
 * of this type.
 */
internal class SampleSourceFile private constructor(private val file: File) {
    private val language: ProgrammingLanguage = ProgrammingLanguage(this.file)

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
                    it matches this.language.packageRegex -> identifier +=
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

    /** Contains static declarations for the [SampleSourceFile] type. */
    companion object {
        /**
         * Creates an instance of [SampleSourceFile] from the specified [file],
         * or returns `null` if the [file] is not in the sample source set or is
         * unsupported.
         */
        fun orNull(file: File): SampleSourceFile? = try {
            this.orThrow(file)
        } catch (exception: IllegalArgumentException) {
            null
        }

        /**
         * Creates an instance of [SampleSourceFile] from the specified [file],
         * or throws an [IllegalArgumentException] if the [file] is not in the
         * sample source set or is unsupported.
         */
        fun orThrow(file: File): SampleSourceFile {
            val fileIsInSampleSourceSet: Boolean =
                file.path.contains("sample/", ignoreCase = true)
            require(fileIsInSampleSourceSet) {
                "'${file.name}' file should be in a sample source set."
            }
            return SampleSourceFile(file)
        }
    }
}
