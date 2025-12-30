package org.kotools.samples.core

@JvmInline
internal value class SamplePath private constructor(private val text: String) {
    // ------------------------------- Creations -------------------------------

    companion object {
        fun from(text: String): SamplePath {
            require(text.endsWith(".md")) {
                "Sample path must end with 'md' file extension (was: $text)."
            }
            return SamplePath(text)
        }
    }

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = this.text
}
