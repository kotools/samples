package org.kotools.samples.internal

/**
 * Represents an error found in the context of the Kotools Samples Gradle
 * plugin.
 *
 * @constructor Returns an error with the specified [message], or throws an
 * [IllegalArgumentException] if the [message] is [blank][String.isBlank].
 */
@JvmInline
internal value class Error(
    /** The message of this error. */
    val message: String
) {
    init {
        require(this.message.isNotBlank()) { "Blank error message specified." }
    }

    override fun toString(): String = this.message
}
