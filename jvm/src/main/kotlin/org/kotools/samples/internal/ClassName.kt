package org.kotools.samples.internal

/**
 * Represents a class name, which contains words written in Pascal case with
 * only letters.
 *
 * A class name is represented by a text written in
 * [Pascal case](https://en.wikipedia.org/wiki/Camel_case) with only letters.
 * For example, the `A` and the `MyClass` texts are valid class names.
 *
 * For creating an instance of this type, see the methods provided by the
 * [ClassName.Companion] type.
 */
internal class ClassName private constructor() {
    /** Contains static declarations for the [ClassName] type. */
    companion object {
        /**
         * Returns a class name from the specified [text], or returns `null` if
         * the [text] is empty or doesn't contain only letters in
         * [Pascal case](https://en.wikipedia.org/wiki/Camel_case).
         */
        fun orNull(text: String): ClassName? {
            val regex = Regex("""^(?:[A-Z][a-z]*)+$""")
            return if (text matches regex) ClassName() else null
        }
    }
}
