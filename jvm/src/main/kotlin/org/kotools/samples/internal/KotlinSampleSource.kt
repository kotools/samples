package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * @constructor Returns a Kotlin sample source with the specified [file], or
 * throws an [IllegalArgumentException] if the [file] has another extension than
 * `kt` or a name that is not suffixed by `Sample`.
 */
@JvmInline
internal value class KotlinSampleSource(
    /** The file of this Kotlin sample source. */
    val file: File
) {
    init {
        require(this.file.extension == "kt") {
            "Kotlin sample source must have 'kt' file extension (input: $file)."
        }
        require(this.file.nameWithoutExtension.endsWith("Sample")) {
            "Kotlin sample source must have 'Sample' suffix in its file name " +
                    "(input: $file)."
        }
    }

    /**
     * Checks the content of this Kotlin sample source and returns `null` if no
     * error was found. Returns an error if this sample source contains multiple
     * classes, if it doesn't contain a public class, or if it contains a
     * single-expression function.
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
        val publicClassCount: Int = classes.count {
            it.startsWith("public class ") || it.startsWith("class ")
        }
        if (publicClassCount == 0)
            return Error("No public class found in ${this}.")
        val singleExpressionFunctionFound: Boolean = this.file.useLines {
            val regex = Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
            it.map(String::trim)
                .any(regex::containsMatchIn)
        }
        return if (singleExpressionFunctionFound)
            Error("Single-expression function found in ${this}.")
        else null
    }

    override fun toString(): String = "'${this.file}' Kotlin sample source"

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object
}
