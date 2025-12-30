package org.kotools.samples.core

internal class KotlinSample private constructor(
    val identifier: SampleIdentifier,
    val content: String
) {
    // ------------------------------- Creations -------------------------------

    companion object Companion {
        fun from(identifier: SampleIdentifier, content: String): KotlinSample {
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

    fun markdownFilePath(): String = this.identifier.toSamplePath()
        .toString()

    fun markdownFileContent(): String = """
        |```kotlin
        |${this.content}
        |```
    """.trimMargin()

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = "'${this.identifier}' Kotlin sample"
}
