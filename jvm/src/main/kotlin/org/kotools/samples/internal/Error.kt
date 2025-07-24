package org.kotools.samples.internal

import java.io.File

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
        // ---------------------- Common factory methods -----------------------

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

        // ----------------------- File factory methods ------------------------

        /**
         * Returns an error indicating that multiple classes were found in the
         * specified [file].
         */
        fun multipleClassesFoundIn(file: File): Error =
            this.orThrow("Multiple classes found in '$file'.")

        /**
         * Returns an error indicating that no public class was found in the
         * specified [file].
         */
        fun noPublicClassFoundIn(file: File): Error =
            this.orThrow("No public class found in '$file'.")

        /**
         * Returns an error indicating that a single-expression Kotlin function
         * was found in the specified [file].
         */
        fun singleExpressionKotlinFunctionFoundIn(file: File): Error =
            this.orThrow("Single-expression Kotlin function found in '$file'.")
    }
}
