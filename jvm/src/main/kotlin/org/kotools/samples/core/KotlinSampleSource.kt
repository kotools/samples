package org.kotools.samples.core

import java.io.File

/** Contains declarations related to Kotlin sample sources. */
internal object KotlinSampleSource {
    /**
     * Returns `true` if the specified [file] has a name suffixed by
     * `Sample.kt`, or returns `false` otherwise.
     */
    fun isValid(file: File): Boolean = file.name.endsWith("Sample.kt")

    /**
     * Returns `true` if the specified [line] contains the `class` Kotlin
     * keyword, or returns `false` otherwise.
     */
    fun isClass(line: String): Boolean = "class " in line
}
