package org.kotools.samples.internal

// ------------------------ Kotlin & Java declarations -------------------------

internal fun String.isPackage(): Boolean =
    this matches Regex("""^package [a-z]+(?:\.[a-z]+)*;?$""")

internal fun String.isClass(): Boolean =
    Regex("""class (?:[A-Z][a-z]*)+""") in this

// ---------------------------- Kotlin declarations ----------------------------

internal fun String.isKotlinPublicClass(): Boolean {
    if (!this.isClass()) return false
    return this.startsWith("public class ") || this.startsWith("class ")
}

internal fun String.isKotlinSingleExpressionFunction(): Boolean =
    this matches Regex("""^fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")

// ----------------------------- Java declarations -----------------------------

internal fun String.isJavaPublicClass(): Boolean =
    this.isClass() && this.startsWith("public class ")
