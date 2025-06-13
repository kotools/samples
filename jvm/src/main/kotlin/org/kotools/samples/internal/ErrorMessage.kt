package org.kotools.samples.internal

import java.io.File

/**
 * Represents an error message.
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [ErrorMessage.Companion] type.
 */
internal class ErrorMessage private constructor(private val text: String) {
    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this error message. */
    override fun toString(): String = this.text

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [ErrorMessage] type. */
    companion object {
        /**
         * Returns an error message indicating that multiple classes were found
         * in the specified [file].
         */
        fun multipleClassesFoundIn(file: File): ErrorMessage =
            ErrorMessage("Multiple classes found in '$file'.")

        /**
         * Returns an error message indicating that no public class was found in
         * the specified [file].
         */
        fun noPublicClassFoundIn(file: File): ErrorMessage =
            ErrorMessage("No public class found in '$file'.")
    }
}
