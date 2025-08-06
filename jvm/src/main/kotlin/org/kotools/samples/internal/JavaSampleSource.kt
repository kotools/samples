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
    // ----------------------- File's content operations -----------------------

    /**
     * Checks the content of this Java sample source and returns `null` if no
     * error was found. Returns an error if multiple classes were found or if no
     * public class was found.
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
        val publicClassCount: Int =
            classes.count { it.startsWith("public class") }
        return if (publicClassCount == 1) null
        else Error.orThrow("No public class found in $this.")
    }

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
