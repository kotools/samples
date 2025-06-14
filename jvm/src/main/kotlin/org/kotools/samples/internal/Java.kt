package org.kotools.samples.internal

/**
 * Represents the [Java](https://www.java.com) programming language.
 *
 * @constructor Returns the [Java] programming language.
 */
internal class Java : ProgrammingLanguage {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [Java], or returns
     * `false` otherwise.
     */
    override fun equals(other: Any?): Boolean = other is Java

    /** Returns a hash code value for this programming language. */
    override fun hashCode(): Int = this.toString()
        .hashCode()

    // -------------------------- Package declaration --------------------------

    override val packageKeyword: String = "package"

    override val packageRegex: Regex =
        Regex("^${this.packageKeyword} [a-z]+(?:\\.[a-z]+)*;\$")

    override fun isPackageDeclaration(text: String): Boolean =
        Regex("""^package [a-z]+(?:\.[a-z]+)*;$""")
            .matches(text)

    // --------------------------- Class declaration ---------------------------

    override fun isClassDeclaration(text: String): Boolean {
        val regex = Regex("""class [A-Z][A-Za-z]* \{""")
        return regex in text
    }

    override fun isPublicClassDeclaration(text: String): Boolean =
        this.isClassDeclaration(text) && text.startsWith("public class ")

    override fun classNameOrNull(text: String): String? =
        if (!this.isClassDeclaration(text)) null
        else text.substringAfter("class ")
            .substringBefore(" {")

    // ------------------------- Function declaration --------------------------

    // This is not a keyword but the type to return!
    override val functionKeyword: String = "void"

    override val functionHeaderRegex: Regex =
        Regex("${this.functionKeyword} [A-Za-z_]+\\(\\) \\{\$")

    // -------------------------- Markdown operations --------------------------

    override val markdownIdentifier: String = "java"

    // ------------------------------ Conversions ------------------------------

    /** Returns the name of this programming language. */
    override fun toString(): String = "Java"
}
