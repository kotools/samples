package org.kotools.samples.internal

import java.io.File

/**
 * Represents an exception message.
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [ExceptionMessage.Companion] type.
 */
internal class ExceptionMessage private constructor(private val text: String) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [ExceptionMessage]
     * with the same [string representation][ExceptionMessage.toString] as this
     * exception message, or returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean =
        other is ExceptionMessage && other.text == this.text

    /** Returns a hash code value for this exception message. */
    override fun hashCode(): Int = this.text.hashCode()

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this exception message. */
    override fun toString(): String = this.text

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [ExceptionMessage] type. */
    companion object {
        /**
         * Returns an exception message with the specified [text], or throws an
         * [IllegalArgumentException] if the [text] is [blank][String.isBlank].
         */
        fun orThrow(text: String): ExceptionMessage {
            require(text.isNotBlank()) { "Blank exception message ('$text')." }
            return ExceptionMessage(text)
        }

        /**
         * Returns an exception message indicating that multiple classes were
         * found in the specified [file].
         */
        fun multipleClassesFoundIn(file: File): ExceptionMessage =
            this.orThrow("Multiple classes found in '$file'.")

        /**
         * Returns an exception message indicating that no public class was
         * found in the specified [file].
         */
        fun noPublicClassFoundIn(file: File): ExceptionMessage =
            ExceptionMessage("No public class found in '$file'.")
    }
}
