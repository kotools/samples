package org.kotools.samples.internal

/**
 * Represents the [Kotlin](https://kotlinlang.org) programming language.
 *
 * @constructor Returns the [Kotlin] programming language.
 */
internal class Kotlin : ProgrammingLanguage {
    override val classKeyword: String = "class"
    override val classHeaderRegex: Regex =
        Regex("${this.classKeyword} [A-Z][A-Za-z]*")
    override val fileExtension: String = "kt"
    override val functionKeyword: String = "fun"
    override val functionHeaderRegex: Regex =
        Regex("$functionKeyword [A-Za-z_]+\\(\\) \\{\$")
    override val markdownIdentifier: String = "kotlin"
    override val packageKeyword: String = "package"
    override val packageRegex: Regex =
        Regex("^${this.packageKeyword} [a-z]+(?:\\.[a-z]+)*\$")

    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [Kotlin], or
     * returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean = other is Kotlin

    /** Returns a hash code value for this programming language. */
    override fun hashCode(): Int = this.toString()
        .hashCode()

    // ------------------------------ Conversions ------------------------------

    /** Returns the name of this programming language. */
    override fun toString(): String = "Kotlin"
}
