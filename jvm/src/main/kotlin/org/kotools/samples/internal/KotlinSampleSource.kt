package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * @constructor Returns a Kotlin sample source with the specified [file], or
 * throws an [IllegalArgumentException] if the [file] has another extension than
 * `kt` or a name that is not suffixed by `Sample`.
 */
@JvmInline
internal value class KotlinSampleSource(
    /** The file of this Kotlin sample source. */
    val file: File
) {
    init {
        require(this.file.extension == "kt") {
            "Kotlin sample source must have 'kt' file extension (input: $file)."
        }
        require(this.file.nameWithoutExtension.endsWith("Sample")) {
            "Kotlin sample source must have 'Sample' suffix in its file name " +
                    "(input: $file)."
        }
    }

    override fun toString(): String = "'${this.file}' Kotlin sample source"
}
