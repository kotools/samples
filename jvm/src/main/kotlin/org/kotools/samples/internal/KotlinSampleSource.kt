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
         * Returns the specified [file] as a Kotlin sample source, or returns
         * `null` if the [file]'s extension is other than `kt`, if the [file] is
         * not located in a test Kotlin source set, or the [file]'s name is not
         * suffixed by `Sample`.
         */
        fun orNull(file: File): KotlinSampleSource? {
            val fileIsValid: Boolean = file.extension == "kt"
                    && file.path.contains("test/kotlin/", ignoreCase = true)
                    && file.nameWithoutExtension.endsWith("Sample")
            return if (fileIsValid) KotlinSampleSource() else null
        }
    }
}
