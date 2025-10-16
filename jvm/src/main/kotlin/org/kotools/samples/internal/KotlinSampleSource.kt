package org.kotools.samples.internal

import java.io.File

/** Contains declarations related to Kotlin sample sources. */
internal object KotlinSampleSource {
    /**
     * Returns `true` if the specified [file] has a name suffixed by
     * `Sample.kt`, or returns `false` otherwise.
     */
    fun isValid(file: File): Boolean = file.name.endsWith("Sample.kt")

    /**
     * Returns `true` if the specified [line] contains the `class` or the `fun`
     * Kotlin keywords, or returns `false` otherwise.
     */
    fun isClassOrFunction(line: String): Boolean =
        this.isClass(line) || this.isFunction(line)

    private fun isClass(line: String): Boolean = "class " in line

    private fun isFunction(line: String): Boolean = "fun " in line

    /**
     * Checks the classes and the functions from the specified Kotlin
     * [declarations] included in the specified [file], and returns `null` if no
     * error was found. Returns an error if the [declarations] parameter
     * contains multiple classes, if it doesn't contain a public class, or if it
     * contains a single-expression function.
     */
    fun classFunctionError(file: File, declarations: List<String>): String? {
        val classes: List<String> = declarations.filter(this::isClass)
        if (classes.count() > 1)
            return "Multiple classes found in ${this.toString(file)}."
        val publicClassCount: Int = classes.count {
            it.startsWith("public class ") || it.startsWith("class ")
        }
        if (publicClassCount == 0)
            return "No public class found in ${this.toString(file)}."
        val regex = Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
        val singleExpressionFunctionFound: Boolean = declarations
            .filter(this::isFunction)
            .any(regex::containsMatchIn)
        return if (singleExpressionFunctionFound)
            "Single-expression function found in ${this.toString(file)}."
        else null
    }

    private fun toString(file: File): String =
        "'${file.name}' Kotlin sample source"
}
