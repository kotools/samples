package org.kotools.samples.internal

/**
 * Returns `true` if this string contains the `class` keyword followed by a
 * class name, or returns `false` otherwise.
 *
 * This function is compatible with [Kotlin](https://kotlinlang.org) and
 * [Java](https://www.java.com), due to their similar syntax for declaring a
 * class.
 *
 * Here's an example of declaring a class in [Kotlin](https://kotlinlang.org):
 *
 * ```kotlin
 * public class Something
 * ```
 *
 * Here's an example of declaring the same class in
 * [Java](https://www.java.com):
 *
 * ```java
 * public class Something {}
 * ```
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
 * The [String.isPublicClassDeclarationInJava] function does a similar check on
 * the specified string, but for code written in [Java](https://www.java.com)
 * instead.
 *
 * See the [String.isClassDeclaration] function for checking that the specified
 * string is a class declaration, independently of its visibility modifier.
 */
internal fun String.isPublicClassDeclarationInKotlin(): Boolean {
    if (!this.isClassDeclaration()) return false
    return this.startsWith("public class") || this.startsWith("class")
}

/**
 * Returns `true` if this string represents a public class declaration in
 * [Java](https://www.java.com), or returns `false` otherwise.
 *
 * In this programming language, a class declaration is public if it has the
 * `public` access level modifier. See the
 * [Java access level modifiers](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
 * documentation for more details about their usage.
 *
 * ```java
 * public class PublicJavaClass {}
 * ```
 *
 * The [String.isPublicClassDeclarationInKotlin] function does a similar check
 * on the specified string, but for code written in
 * [Kotlin](https://kotlinlang.org) instead.
 *
 * See the [String.isClassDeclaration] function for checking that the specified
 * string is a class declaration, independently of its access level modifier.
 */
internal fun String.isPublicClassDeclarationInJava(): Boolean =
    this.isClassDeclaration() && this.startsWith("public class")
