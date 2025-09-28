package org.kotools.samples.internal

import java.io.File

/** Represents a sample source written in [Java](https://www.java.com). */
@JvmInline
internal value class JavaSampleSource private constructor(
    /** The file of this Java sample source. */
    val file: File
) {
    /**
     * Checks the content of this Java sample source and returns `null` if no
     * error was found. Returns an error if this sample source contains multiple
     * classes, or if it doesn't contain a public class.
     */
    fun contentError(): Error? {
        val classes: List<String> = this.file.useLines {
            val regex = Regex("""class (?:[A-Z][a-z]*)+""")
            it.map(String::trim)
                .filter(regex::containsMatchIn)
                .toList()
        }
        if (classes.count() > 1)
            return Error("Multiple classes found in ${this}.")
        val publicClassCount: Int =
            classes.count { it.startsWith("public class ") }
        return if (publicClassCount == 0)
            Error("No public class found in ${this}.")
        else null
    }

    override fun toString(): String = "'${this.file}' Java sample source"

    /** Contains static declarations for the [JavaSampleSource] type. */
    companion object {
        /**
         * Returns a Java sample source with the specified [file], or returns
         * `null` if the [file] has another extension than `java` or a name that
         * is not suffixed by `Sample`.
         */
        fun orNull(file: File): JavaSampleSource? = file
            .takeIf { it.extension == "java" }
            ?.takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.let(::JavaSampleSource)

        /**
         * Returns a Java sample source with the specified [file], or throws an
         * [IllegalArgumentException] if the [file] has another extension than
         * `java` or a name that is not suffixed by `Sample`.
         */
        fun orThrow(file: File): JavaSampleSource {
            require(file.extension == "java") {
                "Java sample source must have 'java' file extension (input: " +
                        "$file)."
            }
            require(file.nameWithoutExtension.endsWith("Sample")) {
                "Java sample source must have 'Sample' suffix in its file " +
                        "name (input: $file)."
            }
            return JavaSampleSource(file)
        }
    }
}
