package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source file written in [Kotlin](https://kotlinlang.org/).
 */
internal class KotlinSampleSourceFile private constructor() {
    /** Contains static declarations for the [KotlinSampleSourceFile] type. */
    companion object {
        /**
         * Returns the specified [file] as a Kotlin sample source file. Returns
         * `null` if the [file] is not in a test Kotlin source set, if its name
         * is not suffixed by `Sample`, or if its extension is other than `kt`.
         */
        fun orNull(file: File): KotlinSampleSourceFile? =
            if (file.extension == "kt" &&
                file.nameWithoutExtension.endsWith("Sample") &&
                file.path.contains("test/", ignoreCase = true)
            ) {
                KotlinSampleSourceFile()
            } else null
    }
}
