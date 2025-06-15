package org.kotools.samples.internal

/**
 * Represents the [Kotlin](https://kotlinlang.org) programming language.
 *
 * @constructor Returns the [Kotlin] programming language.
 */
internal class Kotlin : ProgrammingLanguage {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [Kotlin], or
     * returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean = other is Kotlin

    /** Returns a hash code value for this programming language. */
    override fun hashCode(): Int = this.toString()
        .hashCode()

    // -------------------------- Package declaration --------------------------

    override val packageKeyword: String = "package"

    override fun isPackageDeclaration(text: String): Boolean =
        Regex("""^package [a-z]+(?:\.[a-z]+)*$""")
            .matches(text)

    // --------------------------- Class declaration ---------------------------

    override fun isClassDeclaration(text: String): Boolean {
        val regex = Regex("""class [A-Z][A-Za-z]*""")
        return regex in text
    }

    override fun isPublicClassDeclaration(text: String): Boolean {
        if (!this.isClassDeclaration(text)) return false
        return text.startsWith("public class ") || text.startsWith("class ")
    }

    override fun classNameOrNull(text: String): String? =
        if (!this.isClassDeclaration(text)) null
        else text.substringAfter("class ")
            .substringBefore(" {")

    // ------------------------- Function declaration --------------------------

    override val functionKeyword: String = "fun"

    override val functionHeaderRegex: Regex =
        Regex("$functionKeyword [A-Za-z_]+\\(\\) \\{\$")

    // -------------------------- Markdown operations --------------------------

    override val markdownIdentifier: String = "kotlin"

    // ------------------------------ Conversions ------------------------------

    /** Returns the name of this programming language. */
    override fun toString(): String = "Kotlin"
}
