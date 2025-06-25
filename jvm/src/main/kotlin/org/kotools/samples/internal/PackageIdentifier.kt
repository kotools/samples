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
         *
         * See the [orThrow] method for throwing an exception instead of
         * returning `null` in case of invalid [text].
         */
        fun orNull(text: String): PackageIdentifier? {
            if (text.isEmpty()) return null
            val textHasOnlyLowercaseLetters: Boolean = text.split('.')
                .all { it.all(Char::isLowerCase) }
            return if (textHasOnlyLowercaseLetters) PackageIdentifier()
            else null
        }

        /**
         * Returns a package identifier from the specified [text], or throws an
         * [IllegalArgumentException] if the [text] is empty or doesn't contain
         * words, with only lowercase letters, separated by a dot (`.`) if
         * multiple.
         *
         * See the [orNull] method for returning `null` instead of throwing an
         * exception in case of invalid [text].
         */
        fun orThrow(text: String): PackageIdentifier {
            require(text.isNotEmpty()) {
                "Package identifier must be non-empty."
            }
            val textHasOnlyLowercaseLetters: Boolean = text.split('.')
                .all { it.all(Char::isLowerCase) }
            require(textHasOnlyLowercaseLetters) {
                "Package identifier's characters must be lowercase letters " +
                        "(input: '$text')."
            }
            return PackageIdentifier()
        }
    }
}
