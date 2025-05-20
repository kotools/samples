package org.kotools.samples.internal

import java.io.File

/**
 * Represents a [programming language](https://en.wikipedia.org/wiki/Programming_language).
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [ProgrammingLanguage.Companion] type.
 */
internal sealed interface ProgrammingLanguage {
    val functionHeaderRegex: Regex
    val functionKeyword: String
    val markdownIdentifier: String
    val packageKeyword: String
    val packageRegex: Regex

    // --------------------------- Class declaration ---------------------------

    val classKeyword: String
    val classHeaderRegex: Regex

    /**
     * Returns `true` if the specified [text] represents a public class
     * declaration written in this programming language, or returns `false`
     * otherwise.
     */
    fun isPublicClassDeclaration(text: String): Boolean

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [ProgrammingLanguage] type. */
    companion object {
        /**
         * Returns the programming language used by the specified [file], or
         * returns `null` if the [file] is unsupported.
         */
        fun orNull(file: File): ProgrammingLanguage? = when (file.extension) {
            "java" -> Java()
            "kt" -> Kotlin()
            else -> null
        }
    }
}
