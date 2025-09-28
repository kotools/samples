package org.kotools.samples.internal

/**
 * Represents an error found in the context of the Kotools Samples Gradle
 * plugin.
 */
@JvmInline
internal value class Error(
    /** The message of this error. */
    val message: String
) {
    override fun toString(): String = this.message
}
