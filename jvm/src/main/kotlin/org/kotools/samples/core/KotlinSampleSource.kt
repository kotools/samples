package org.kotools.samples.core

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [KotlinSampleSource.Companion] type.
 */
@JvmInline
internal value class KotlinSampleSource private constructor(
    private val file: File
) {
    override fun toString(): String = "'${this.file.name}' Kotlin sample source"

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns a Kotlin sample source with the specified [file], or returns
         * `null` if the [file]'s name doesn't end with `Sample.kt`.
         */
        infix fun of(file: File): KotlinSampleSource? = file
            .takeIf { it.name.endsWith("Sample.kt") }
            ?.let(::KotlinSampleSource)
    }
}
