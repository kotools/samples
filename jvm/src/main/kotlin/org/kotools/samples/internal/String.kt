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

/**
 * Returns `true` if this string represents a public class declaration written
 * in [Kotlin](https://kotlinlang.org), or returns `false` otherwise.
 *
 * In this programming language, a class declaration is public if it doesn't
 * have a visibility modifier, or if it has the `public` visibility modifier.
 * See the
 * [Kotlin visibility modifiers](https://kotlinlang.org/docs/visibility-modifiers.html)
 * documentation for more details about their usage.
 *
 * ```kotlin
 * class PublicClassWithoutVisibilityModifier
 * public class PublicClassWithVisibilityModifier
 * ```
 *
 * See the [String.isClassDeclaration] function for checking that the specified
 * string is a class declaration, independently of its visibility modifier.
 */
internal fun String.isPublicClassDeclarationInKotlin(): Boolean {
    if (!this.isClassDeclaration()) return false
    return this.startsWith("public class") || this.startsWith("class")
}
