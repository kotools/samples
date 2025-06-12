package org.kotools.samples.internal

// ------------------------- Package-related functions -------------------------

/**
 * Returns `true` if this string starts with the `package` keyword followed by a
 * package identifier, or returns `false` otherwise.
 *
 * A package identifier consists of one or multiple words separated by a dot
 * (`.`).
 *
 * This function is compatible with [Kotlin](https://kotlinlang.org) and
 * [Java](https://www.java.com), due to their similar syntax for declaring a
 * package.
 *
 * Here's an example of declaring a package in [Kotlin](https://kotlinlang.org):
 *
 * ```kotlin
 * package org.kotools.samples
 * ```
 *
 * Here's an example of declaring the same package in
 * [Java](https://www.java.com):
 *
 * ```java
 * package org.kotools.samples;
 * ```
 */
internal fun String.isPackageDeclaration(): Boolean {
    val regex = Regex("""^package [a-z]+(?:\.[a-z]+)*;?$""")
    return this matches regex
}

/**
 * Returns the package identifier specified in this string, or returns `null` if
 * this string doesn't represent a package declaration.
 *
 * This function is compatible with [Kotlin](https://kotlinlang.org) and
 * [Java](https://www.java.com), due to their similar syntax for declaring a
 * package.
 *
 * See the [String.isPackageDeclaration] function for more details about package
 * declarations represented with strings.
 */
internal fun String.packageIdentifierOrNull(): String? =
    if (!this.isPackageDeclaration()) null
    else this.substringAfter("package ")
        .substringBefore(';')

// -------------------------- Class-related functions --------------------------

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

/**
 * Returns the name of the class declared in this string, or returns `null` if
 * this string is not a class declaration.
 *
 * This function is compatible with [Kotlin](https://kotlinlang.org) and
 * [Java](https://www.java.com), due to their similar syntax for declaring a
 * class.
 *
 * See the [String.isClassDeclaration] function for more details about class
 * declarations represented with strings.
 */
internal fun String.classNameOrNull(): String? =
    if (!this.isClassDeclaration()) null
    else this.substringAfter("class ")
        .substringBefore(" {")

// ------------------------ Function-related functions -------------------------

/**
 * Returns `true` if this string represents a test function header written in
 * [Kotlin](https://kotlinlang.org), or returns `false` otherwise.
 *
 * In this programming language, a function header starts with the `fun`
 * keyword, followed by the name of the function and a pair of parentheses.
 *
 * ```kotlin
 * fun helloWorld() { // <- here's the function header
 *     println("Hello World")
 * }
 * ```
 *
 * See the [Kotlin functions](https://kotlinlang.org/docs/functions.html)
 * documentation for more details about their syntax.
 */
internal fun String.isTestFunctionHeaderInKotlin(): Boolean {
    val regex = Regex("""fun [A-Za-z_]+\(\) \{$""")
    return regex in this
}

/**
 * Returns `true` if this string represents a test function header written in
 * [Java](https://www.java.com), or returns `false` otherwise.
 *
 * In this programming language, a test function header starts with the `void`
 * return type, followed by the name of the function and a pair of parentheses.
 *
 * ```java
 * void helloWorld() { // <- here's the function header
 *     System.out.println("Hello World");
 * }
 * ```
 *
 * See the [Java functions](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
 * documentation for more details about their syntax.
 */
internal fun String.isTestFunctionHeaderInJava(): Boolean {
    val regex = Regex("""void [A-Za-z_]+\(\) \{$""")
    return regex in this
}

/**
 * Returns the name of the [Kotlin](https://kotlinlang.org) test function
 * declared in this string, or returns `null` if this string doesn't represent a
 * test function header written in this programming language.
 *
 * See the [String.isTestFunctionHeaderInKotlin] function for more details about
 * test function headers represented as strings.
 */
internal fun String.testFunctionNameInKotlinOrNull(): String? =
    if (!this.isTestFunctionHeaderInKotlin()) null
    else this.substringBefore('(')
        .substringAfter("fun ")

/**
 * Returns the name of the [Java](https://www.java.com) test function declared
 * in this string, or returns `null` if this string doesn't represent a test
 * function header written in this programming language.
 *
 * See the [String.isTestFunctionHeaderInJava] function for more details about
 * test function headers represented as strings.
 */
internal fun String.testFunctionNameInJavaOrNull(): String? =
    if (!this.isTestFunctionHeaderInJava()) null
    else this.substringBefore('(')
        .substringAfter("void ")
