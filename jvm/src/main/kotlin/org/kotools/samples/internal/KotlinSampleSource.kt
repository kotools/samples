package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [KotlinSampleSource.Companion] type.
 */
internal class KotlinSampleSource private constructor(private val file: File) {
    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Kotlin sample source. */
    override fun toString(): String = "'${this.file.name}' Kotlin sample source"

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns a Kotlin sample source from the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample`, or if the
         * [file] has another extension than `kt`.
         *
         * For throwing an exception instead of returning `null` in case of
         * invalid [file], see the [orThrow] function instead.
         */
        fun orNull(file: File): KotlinSampleSource? = file
            .takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.takeIf { it.extension == "kt" }
            ?.let(::KotlinSampleSource)

        /**
         * Returns a Kotlin sample source from the specified [file], or throws
         * an [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample`, or if the [file] has another extension than `kt`.
         *
         * For returning `null` instead of throwing an exception in case of
         * invalid [file], see the [orNull] function instead.
         */
        fun orThrow(file: File): KotlinSampleSource {
            val fileName: String = file.nameWithoutExtension
            require(fileName.endsWith("Sample")) {
                "'$fileName' must ends with 'Sample'."
            }
            val fileExtension: String = file.extension
            require(fileExtension == "kt") { "'$fileExtension' must be 'kt'." }
            return KotlinSampleSource(file)
        }
    }
}
