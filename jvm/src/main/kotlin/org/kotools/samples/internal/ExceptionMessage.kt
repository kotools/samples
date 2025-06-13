package org.kotools.samples.internal

import java.io.File

/**
 * Represents an exception message.
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [ExceptionMessage.Companion] type.
 */
internal class ExceptionMessage private constructor(private val text: String) {
    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this exception message. */
    override fun toString(): String = this.text

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [ExceptionMessage] type. */
    companion object {
        /**
         * Returns an exception message indicating that multiple classes were
         * found in the specified [file].
         */
        fun multipleClassesFoundIn(file: File): ExceptionMessage =
            ExceptionMessage("Multiple classes found in '$file'.")

        /**
         * Returns an exception message indicating that no public class was
         * found in the specified [file].
         */
        fun noPublicClassFoundIn(file: File): ExceptionMessage =
            ExceptionMessage("No public class found in '$file'.")
    }
}
