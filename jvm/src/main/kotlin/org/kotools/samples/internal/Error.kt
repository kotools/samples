package org.kotools.samples.internal

/**
 * Represents an error.
 *
 * For creating an instance of this type, see the methods provided by the
 * [Error.Companion] type.
 */
@JvmInline
internal value class Error private constructor(
    /** The message of this error. */
    val message: String
) {
    /** Returns the [message] of this error. */
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
            require(message.isNotBlank()) { "Blank error message." }
            return Error(message)
        }
    }
}
