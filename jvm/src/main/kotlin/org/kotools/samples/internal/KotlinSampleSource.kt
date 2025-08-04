package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in Kotlin.
 *
 * See the methods provided by the [KotlinSampleSource.Companion] type for
 * creating a Kotlin sample source.
 */
@JvmInline
internal value class KotlinSampleSource private constructor(
    /** The file corresponding to this Kotlin sample source. */
    val file: File
) {
    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Kotlin sample source. */
    override fun toString(): String = "'${this.file}' Kotlin sample source"

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        private const val FILE_NAME_SUFFIX: String = "Sample"
        private const val FILE_EXTENSION: String = "kt"

        /**
         * Returns a Kotlin sample source with the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample.kt`.
         */
        fun orNull(file: File): KotlinSampleSource? = file
            .takeIf { it.extension == this.FILE_EXTENSION }
            ?.takeIf { it.nameWithoutExtension.endsWith(this.FILE_NAME_SUFFIX) }
            ?.let(::KotlinSampleSource)

        /**
         * Returns a Kotlin sample source with the specified [file], or throws
         * an [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample.kt`.
         */
        fun orThrow(file: File): KotlinSampleSource {
            val expectedFileExtension: String = this.FILE_EXTENSION
            require(file.extension == expectedFileExtension) {
                "'$file' file extension must be '$expectedFileExtension'."
            }
            val expectedNameSuffix: String = this.FILE_NAME_SUFFIX
            require(file.nameWithoutExtension.endsWith(expectedNameSuffix)) {
                "'$file' file name must be suffixed by '$expectedNameSuffix'."
            }
            return KotlinSampleSource(file)
        }
    }
}
