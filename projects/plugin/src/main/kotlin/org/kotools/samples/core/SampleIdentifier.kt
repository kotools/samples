package org.kotools.samples.core

@JvmInline
internal value class SampleIdentifier private constructor(
    private val text: String
) {
    // ------------------------------- Creations -------------------------------

    companion object {
        fun from(text: String): SampleIdentifier {
            val textIsValid: Boolean = text.split('.')
                .all { it.all(Char::isLetter) }
            require(textIsValid) {
                "Sample identifier must contain letters separated by dot " +
                        "(was: $text)."
            }
            return SampleIdentifier(text)
        }
    }

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = this.text

    fun toSamplePath(): SamplePath {
        val path: String = this.text.replace(oldChar = '.', newChar = '/')
            .plus(".md")
        return SamplePath.from(path)
    }
}
