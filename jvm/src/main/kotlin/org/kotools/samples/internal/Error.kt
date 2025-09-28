package org.kotools.samples.internal

// ----------------------------- Factory functions -----------------------------

/**
 * Returns an error with this string as message, or throws an
 * [IllegalArgumentException] if this string is blank.
 */
internal fun String.toError(): Error {
    require(this.isNotBlank()) { "Blank error's message specified." }
    return Error(message = this)
}

// ----------------------------------- Type ------------------------------------

/**
 * Represents an error found in the context of the Kotools Samples Gradle
 * plugin.
 */
@JvmInline
internal value class Error internal constructor(
    /** The message of this error. */
    val message: String
) {
    override fun toString(): String = this.message
}
