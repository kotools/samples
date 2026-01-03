package org.kotools.samples.core

internal class KotlinSample private constructor(
    val identifier: SampleIdentifier,
    private val content: String
) {
    // ------------------------------- Creations -------------------------------

    companion object Companion {
        fun from(identifier: SampleIdentifier, content: String): KotlinSample =
            KotlinSample(
                identifier,
                content.ifBlank { """TODO("Sample is not yet implemented.")""" }
            )
    }

    // -------------------- Structural equality operations ---------------------

    override fun equals(other: Any?): Boolean =
        other is KotlinSample && this.identifier == other.identifier

    override fun hashCode(): Int = this.identifier.hashCode()

    // -------------------------- Markdown operations --------------------------

    fun path(): SamplePath = this.identifier.toSamplePath()

    fun markdownFileContent(): String = """
        |```kotlin
        |${this.content}
        |```
    """.trimMargin()

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = "'${this.identifier}' Kotlin sample"
}
