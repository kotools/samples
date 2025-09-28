package org.kotools.samples.internal

import java.io.File

// ----------------------------- Factory functions -----------------------------

/**
 * Returns a Kotlin sample source with this file, or returns `null` if this
 * file's name doesn't end with `Sample.kt`.
 */
internal fun File.toKotlinSampleSourceOrNull(): KotlinSampleSource? = this
    .takeIf { it.name.endsWith("Sample.kt") }
    ?.let(::KotlinSampleSource)

// ----------------------------------- Type ------------------------------------

/** Represents a sample source written in [Kotlin](https://kotlinlang.org). */
@JvmInline
internal value class KotlinSampleSource internal constructor(
    /** The file of this Kotlin sample source. */
    val file: File
) {
    override fun toString(): String = "'${this.file}' Kotlin sample source"

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object
}

// ------------------------------ File operations ------------------------------

/**
 * Checks the content of this Kotlin sample source and returns `null` if no
 * error was found. Returns an error if this sample source contains multiple
 * classes, if it doesn't contain a public class, or if it contains a
 * single-expression function.
 */
internal fun KotlinSampleSource.contentError(): Error? {
    val classes: List<String> = this.file.useLines {
        val regex = Regex("""class (?:[A-Z][a-z]*)+""")
        it.map(String::trim)
            .filter(regex::containsMatchIn)
            .toList()
    }
    if (classes.count() > 1)
        return Error.orThrow("Multiple classes found in ${this}.")
    val publicClassCount: Int = classes.count {
        it.startsWith("public class ") || it.startsWith("class ")
    }
    if (publicClassCount == 0)
        return Error.orThrow("No public class found in ${this}.")
    val singleExpressionFunctionFound: Boolean = this.file.useLines {
        val regex = Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
        it.map(String::trim)
            .any(regex::containsMatchIn)
    }
    return if (singleExpressionFunctionFound)
        Error.orThrow("Single-expression function found in ${this}.")
    else null
}
