package org.kotools.samples.core

internal class KotlinSample private constructor(
    private val identifier: String,
    private val content: String
) {
    // ------------------------------- Creations -------------------------------

    companion object Companion {
        fun from(identifier: String, content: String): KotlinSample {
            require(identifier.isNotBlank()) {
                "Kotlin sample's identifier can't be blank."
            }
            val identifierIsQualifiedName: Boolean =
                if ('.' !in identifier) identifier.all(Char::isLetterOrDigit)
                else identifier.split('.')
                    .all { it.all(Char::isLetterOrDigit) }
            require(identifierIsQualifiedName) {
                "Kotlin sample's identifier must be a qualified name (was: " +
                        "$identifier)."
            }
            require(content.isNotBlank()) {
                "Kotlin sample's content can't be blank."
            }
            return KotlinSample(identifier, content)
        }
    }

    // -------------------- Structural equality operations ---------------------

    override fun equals(other: Any?): Boolean =
        other is KotlinSample && this.identifier == other.identifier

    override fun hashCode(): Int = this.identifier.hashCode()

    // -------------------------- Markdown operations --------------------------

    fun markdownFilePath(): String =
        this.identifier.replace(oldChar = '.', newChar = '/') + ".md"

    fun markdownFileContent(): String = """
        |```kotlin
        |${this.content}
        |```
    """.trimMargin()

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = "'${this.identifier}' Kotlin sample"
}
