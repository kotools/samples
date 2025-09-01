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

    /** Contains static declarations for the [Error] type. */
    companion object {
        /**
         * Returns an error with the specified [message], or returns `null` if
         * the [message] is [blank][String.isBlank].
         */
        fun orNull(message: String): Error? = message.takeIf(String::isNotBlank)
            ?.let(::Error)

        /**
         * Returns an error with the specified [message], or throws an
         * [IllegalArgumentException] if the [message] is
         * [blank][String.isBlank].
         */
        fun orThrow(message: String): Error {
            require(message.isNotBlank()) { "Blank error's message specified." }
            return Error(message)
        }
    }
}
