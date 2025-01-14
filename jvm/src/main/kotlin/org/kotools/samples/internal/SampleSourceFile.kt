package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source file.
 *
 * See the [SampleSourceFile.Companion.orNull] function for creating an instance
 * of this type.
 */
internal class SampleSourceFile private constructor(private val file: File) {
    private val language: ProgrammingLanguage = ProgrammingLanguage(this.file)

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

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this sample source file. */
    override fun toString(): String =
        "Sample source file at '${this.file.path}'."

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [SampleSourceFile] type. */
    companion object {
        /**
         * Creates an instance of [SampleSourceFile] from the specified [file].
         * Returns `null` if the [file] is not in a sample or test Kotlin source
         * set, if it is in a test Kotlin source set but its name is not
         * suffixed by `Sample`, or if it is unsupported.
         */
        fun orNull(file: File): SampleSourceFile? = try {
            this.orThrow(file)
        } catch (exception: IllegalArgumentException) {
            null
        }

        private fun orThrow(file: File): SampleSourceFile {
            val fileIsInSampleSourceSet: Boolean =
                file.path.contains("sample/", ignoreCase = true)
            val fileIsInTestSourceSet: Boolean =
                file.path.contains("test/", ignoreCase = true)
            require(fileIsInSampleSourceSet || fileIsInTestSourceSet) {
                "'${file.name}' file should be in a sample or test source set."
            }
            if (fileIsInSampleSourceSet) return SampleSourceFile(file)
            val suffix = "Sample"
            val fileNameHasValidSuffix: Boolean =
                file.nameWithoutExtension.endsWith(suffix)
            require(fileNameHasValidSuffix) {
                "'${file.name}' file's name should be suffixed by '$suffix'."
            }
            return SampleSourceFile(file)
        }
    }
}
