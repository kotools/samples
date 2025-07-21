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

internal fun String.isKotlinFunction(): Boolean =
    Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)?""") in this

internal fun String.isKotlinSingleExpressionFunction(): Boolean =
    this.isKotlinFunction() && Regex(""" = .+$""") in this

internal fun String.kotlinFunctionName(): String {
    require(this.isKotlinFunction()) {
        "String is not a function header (input: '$this')."
    }
    return this.substringAfter("fun ")
        .substringBefore('(')
}

internal fun String.isSampleIdentifier(): Boolean =
    if (this.isBlank() || '.' !in this) false
    else this.split('.')
        .all { it.all(Char::isLetterOrDigit) }

internal fun String.isSampleReference(): Boolean =
    "SAMPLE: [" in this && this.endsWith(']')

internal fun String.sampleIdentifier(): String {
    require(this.isSampleReference()) {
        "String is not a sample reference (input: '$this')."
    }
    return this.substringAfter('[')
        .substringBefore(']')
}

internal fun String.sampleIdentifierToSamplePath(): String {
    require(this.isSampleIdentifier()) {
        "String is not a sample identifier (input: '$this')."
    }
    return this.replace(oldChar = '.', newChar = '/') + ".md"
}

// ----------------------------------- Java ------------------------------------

internal fun String.isJavaPublicClass(): Boolean =
    this.isClass() && this.startsWith("public class ")

internal fun String.isJavaTestFunction(): Boolean =
    Regex("""void [A-Za-z_]+\(\) \{$""") in this

internal fun String.javaTestFunctionName(): String {
    require(this.isJavaTestFunction()) {
        "String is not a Java test function (input: '$this')."
    }
    return this.substringAfter("void ")
        .substringBefore('(')
}

// --------------------------------- Markdown ----------------------------------

internal fun String.toKotlinMarkdownCodeBlock(): String {
    require(this.isNotBlank()) { "Blank string specified (input: '$this')." }
    return """
        |```kotlin
        |$this
        |```
    """.trimMargin()
}

internal fun String.toJavaMarkdownCodeBlock(): String {
    require(this.isNotBlank()) { "Blank string specified (input: '$this')." }
    return """
        |```java
        |$this
        |```
    """.trimMargin()
}
