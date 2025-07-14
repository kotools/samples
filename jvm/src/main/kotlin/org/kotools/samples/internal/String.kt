package org.kotools.samples.internal

// ------------------------------- Kotlin & Java -------------------------------

internal fun String.isPackage(): Boolean =
    this matches Regex("""^package [a-z]+(?:\.[a-z]+)*;?$""")

internal fun String.packageIdentifier(): String {
    require(this.isPackage()) {
        "String is not a package declaration (input: '$this')."
    }
    return this.substringAfter("package ")
        .substringBefore(';')
}

internal fun String.isClass(): Boolean =
    Regex("""class (?:[A-Z][a-z]*)+""") in this

internal fun String.className(): String {
    require(this.isClass()) {
        "String is not a class declaration (input: '$this')."
    }
    return this.substringAfter("class ")
        .substringBefore(" {")
}

// ---------------------------------- Kotlin -----------------------------------

internal fun String.isKotlinPublicClass(): Boolean {
    if (!this.isClass()) return false
    return this.startsWith("public class ") || this.startsWith("class ")
}

internal fun String.isKotlinSingleExpressionFunction(): Boolean =
    this matches Regex("""^fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")

// ----------------------------------- Java ------------------------------------

internal fun String.isJavaPublicClass(): Boolean =
    this.isClass() && this.startsWith("public class ")
