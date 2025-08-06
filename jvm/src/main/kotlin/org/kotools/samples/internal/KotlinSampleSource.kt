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
    // ----------------------- File's content operations -----------------------

    /**
     * Checks the content of this Kotlin sample source and returns `null` if no
     * error was found. Returns an error if multiple classes were found, if no
     * public class was found, or if a single-expression function was found.
     */
    fun contentError(): Error? {
        val classes: List<String> = this.file.useLines {
            val regex = Regex("""class (?:[A-Z][a-z]*)+""")
            it.map(String::trim)
                .filter(regex::containsMatchIn)
                .toList()
        }
        if (classes.count() > 1)
            return Error.orThrow("Multiple classes found in $this.")
        val publicClassCount: Int = classes.count {
            it.startsWith("public class") || it.startsWith("class")
        }
        if (publicClassCount == 0)
            return Error.orThrow("No public class found in $this.")
        val singleExpressionFunctionFound: Boolean = this.file.useLines {
            val regex = Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
            it.map(String::trim)
                .any(regex::containsMatchIn)
        }
        return if (!singleExpressionFunctionFound) null
        else Error.orThrow("Single-expression Kotlin function found in $this.")
    }

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Kotlin sample source. */
    override fun toString(): String = "'${this.file}' Kotlin sample source"

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns a Kotlin sample source with the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample.kt`.
         */
        fun orNull(file: File): KotlinSampleSource? = try {
            this.orThrow(file)
        } catch (_: IllegalArgumentException) {
            null
        }

        /**
         * Returns a Kotlin sample source with the specified [file], or throws
         * an [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample.kt`.
         */
        fun orThrow(file: File): KotlinSampleSource {
            require(file.extension == "kt") {
                "'$file' file extension must be 'kt'."
            }
            require(file.nameWithoutExtension.endsWith("Sample")) {
                "'$file' file name must be suffixed by 'Sample'."
            }
            return KotlinSampleSource(file)
        }
    }
}
