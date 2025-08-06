package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in Java.
 *
 * See the methods provided by the [JavaSampleSource.Companion] type for
 * creating a Java sample source.
 */
@JvmInline
internal value class JavaSampleSource private constructor(
    /** The file corresponding to this Java sample source. */
    val file: File
) {
    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Java sample source. */
    override fun toString(): String = "'${this.file}' Java sample source"

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [JavaSampleSource] type. */
    companion object {
        /**
         * Returns a Java sample source with the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample.java`.
         */
        fun orNull(file: File): JavaSampleSource? = try {
            this.orThrow(file)
        } catch (_: IllegalArgumentException) {
            null
        }

        /**
         * Returns a Java sample source with the specified [file], or throws an
         * [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample.java`.
         */
        fun orThrow(file: File): JavaSampleSource {
            require(file.extension == "java") {
                "'$file' file extension must be 'java'."
            }
            require(file.nameWithoutExtension.endsWith("Sample")) {
                "'$file' file name must be suffixed by 'Sample'."
            }
            return JavaSampleSource(file)
        }
    }
}
