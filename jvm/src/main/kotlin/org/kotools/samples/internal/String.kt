package org.kotools.samples.internal

internal fun String.isKotlinClass(): Boolean =
    Regex("""class (?:[A-Z][a-z]*)+""")
        .containsMatchIn(this)

internal fun String.isKotlinPublicClass(): Boolean {
    if (!this.isKotlinClass()) return false
    return this.startsWith("public class ") || this.startsWith("class ")
}

internal fun String.isKotlinSingleExpressionFunction(): Boolean =
    Regex("""^fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
        .matches(this)
