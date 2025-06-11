package org.kotools.samples.internal

/**
 * Returns `true` if this string contains the `class` keyword followed by a
 * class name, or returns `false` otherwise.
 *
 * This function is compatible with [Kotlin](https://kotlinlang.org) and
 * [Java](https://www.java.com), due to their similar syntax for declaring a
 * class.
 */
internal fun String.isClassDeclaration(): Boolean {
    val regex = Regex("""class [A-Z][A-Za-z]*""")
    return regex in this
}
