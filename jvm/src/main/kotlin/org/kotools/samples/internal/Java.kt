package org.kotools.samples.internal

/**
 * Represents the [Java](https://www.java.com) programming language.
 *
 * @constructor Returns the [Java] programming language.
 */
internal class Java : ProgrammingLanguage {
    override val classKeyword: String = "class"
    override val classHeaderRegex: Regex =
        Regex("${this.classKeyword} [A-Z][A-Za-z]*")
    override val functionKeyword: String = "void"
    override val functionHeaderRegex: Regex =
        Regex("${this.functionKeyword} [A-Za-z_]+\\(\\) \\{\$")
    override val markdownIdentifier: String = "java"
    override val packageKeyword: String = "package"
    override val packageRegex: Regex =
        Regex("^${this.packageKeyword} [a-z]+(?:\\.[a-z]+)*;\$")

    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [Java], or returns
     * `false` otherwise.
     */
    override fun equals(other: Any?): Boolean = other is Java

    /** Returns a hash code value for this programming language. */
    override fun hashCode(): Int = this.toString()
        .hashCode()

    // ------------------------------ Conversions ------------------------------

    /** Returns the name of this programming language. */
    override fun toString(): String = "Java"
}
