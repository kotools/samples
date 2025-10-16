package org.kotools.samples.core

/**
 * Represents an error found in a sample source.
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [SampleSourceError.Companion] type.
 */
@JvmInline
internal value class SampleSourceError private constructor(
    /** The message of this error. */
    val message: String
) {
    override fun toString(): String = this.message

    /** Contains static declarations for the [SampleSourceError] function. */
    companion object {
        /**
         * Returns an error indicating that multiple classes were found in the
         * specified [source].
         */
        fun multipleClassesFoundIn(
            source: KotlinSampleSource
        ): SampleSourceError =
            SampleSourceError("Multiple classes found in ${source}.")

        /**
         * Returns an error indicating that no public class was found in the
         * specified [source].
         */
        fun noPublicClassFoundIn(
            source: KotlinSampleSource
        ): SampleSourceError =
            SampleSourceError("No public class found in ${source}.")

        /**
         * Returns an error indicating that a single-expression Kotlin function
         * was found in the specified [source].
         */
        fun singleExpressionFunctionFoundIn(
            source: KotlinSampleSource
        ): SampleSourceError =
            SampleSourceError("Single-expression function found in ${source}.")
    }
}
