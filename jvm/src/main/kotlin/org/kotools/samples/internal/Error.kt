package org.kotools.samples.internal

/**
 * Represents an error.
 *
 * See the methods provided by the [Error.Companion] type for creating an error.
 */
@JvmInline
internal value class Error private constructor(
    /** The message of this error. */
    val message: String
) {
    /**
     * Returns the string representation of this error, corresponding to its
     * [message].
     */
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
