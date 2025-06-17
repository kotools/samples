package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [KotlinSampleSource.Companion] type.
 */
internal class KotlinSampleSource private constructor() {
    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns a Kotlin sample source from the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample`, or if the
         * [file] has another extension than `kt`.
         */
        fun orNull(file: File): KotlinSampleSource? {
            if (!file.nameWithoutExtension.endsWith("Sample")) return null
            if (file.extension != "kt") return null
            return KotlinSampleSource()
        }
    }
}
