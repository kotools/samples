package org.kotools.samples.core

import java.io.File

@JvmInline
internal value class KotlinSampleSource private constructor(
    private val file: File
) {
    // ------------------------------- Creations -------------------------------

    companion object {
        fun fromFileOrNull(file: File): KotlinSampleSource? =
            if (file.extension != "kt") null
            else KotlinSampleSource(file)
    }

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = "'${this.file}' Kotlin sample source"
}
