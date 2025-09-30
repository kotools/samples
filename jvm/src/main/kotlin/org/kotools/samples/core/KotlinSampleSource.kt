package org.kotools.samples.core

import java.io.File

/** Contains declarations related to Kotlin sample sources. */
internal object KotlinSampleSource {
    /**
     * Returns `true` if the specified [file] has a name suffixed by
     * `Sample.kt`, or returns `false` otherwise.
     */
    fun isValid(file: File): Boolean = file.name.endsWith("Sample.kt")

    /**
     * Returns `true` if the specified [line] contains the `class` Kotlin
     * keyword, or returns `false` otherwise.
     */
    fun isClass(line: String): Boolean = "class " in line

    /**
     * Returns `true` if the specified [line] contains the `fun` Kotlin keyword,
     * or returns `false` otherwise.
     */
    fun isFunction(line: String): Boolean = "fun " in line

    /**
     * Checks the classes and the functions from the specified Kotlin
     * [declarations] included in the specified [file], and returns `null` if no
     * error was found. Returns an error if the [declarations] parameter
     * contains multiple classes, if it doesn't contain a public class, or if it
     * contains a single-expression function.
     */
    fun classFunctionError(file: File, declarations: List<String>): String? {
        val classes: List<String> = declarations.filter(::isClass)
        if (classes.count() > 1) return "Multiple classes found in " +
                "'${file.name}' Kotlin sample source."
        val publicClassCount: Int = classes.count {
            it.startsWith("public class ") || it.startsWith("class ")
        }
        if (publicClassCount == 0) return "No public class found in " +
                "'${file.name}' Kotlin sample source."
        val regex = Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
        val singleExpressionFunctionFound: Boolean = declarations
            .filter(::isFunction)
            .any(regex::containsMatchIn)
        return if (singleExpressionFunctionFound) "Single-expression " +
                "function found in '${file.name}' Kotlin sample source."
        else null
    }
}
