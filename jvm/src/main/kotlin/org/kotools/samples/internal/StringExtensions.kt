@file:JvmName("StringExtensions")

package org.kotools.samples.internal

/**
 * Returns `true` if this string represents a class declaration in
 * [Kotlin](https://kotlinlang.org), or returns `false` otherwise.
 *
 * In this programming language, classes are declared by using the `class`
 * keyword followed by a class name, containing letters in
 * [Pascal case](https://fr.wikipedia.org/wiki/Camel_case).
 *
 * ```kotlin
 * class SampleSource
 * ```
 *
 * See the [Kotlin classes](https://kotlinlang.org/docs/classes.html)
 * documentation for more details about their syntax.
 */
internal fun String.isKotlinClass(): Boolean = Regex("""class [A-Z][A-Za-z]*""")
    .containsMatchIn(this)
