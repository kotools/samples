package org.kotools.samples.internal

/**
 * Represents a package identifier.
 *
 * A package identifier is a text represented by a single word or multiple words
 * separated by a dot (`.`), with only lowercase letters. For example, the
 * `samples` and the `my.samples` texts are valid package identifiers.
 *
 * For creating an instance of this type, see the methods provided by the
 * [PackageIdentifier.Companion] type.
 */
internal class PackageIdentifier private constructor() {
    /** Contains static declarations for the [PackageIdentifier] type. */
    companion object {
        /**
         * Returns a package identifier from the specified [text], or returns
         * `null` if the [text] is empty or doesn't contain words, with only
         * lowercase letters, separated by a dot (`.`) if multiple.
         */
        fun orNull(text: String): PackageIdentifier? {
            if (text.isEmpty()) return null
            val textIsValid: Boolean = text.split('.')
                .all { it.all(Char::isLowerCase) }
            return if (textIsValid) PackageIdentifier() else null
        }
    }
}
