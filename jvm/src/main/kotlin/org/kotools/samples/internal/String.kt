package org.kotools.samples.internal

/**
 * Returns `true` if this string represents a class declaration, or returns
 * `false` otherwise.
 */
internal fun String.isClassDeclaration(): Boolean {
    val regex = Regex("""class [A-Z][A-Za-z]*""")
    return regex in this
}
