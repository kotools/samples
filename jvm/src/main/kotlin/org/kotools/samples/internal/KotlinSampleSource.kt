package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * For creating an instance of this type, see the factory functions provided by
 * the [KotlinSampleSource.Companion] type.
 */
internal class KotlinSampleSource private constructor(private val file: File) {
    /**
     * Checks that this sample source contains a single public class, and throws
     * an [IllegalStateException] if this is not the case.
     */
    fun checkSinglePublicClass() {
        val classCount: Int = this.file.useLines {
            it.map(String::trim)
                .count(this::isPublicClassDeclaration)
        }
        if (classCount == 0) error("No public class found in '${this.file}'.")
        if (classCount > 1)
            error("Multiple public classes found in '${this.file}'.")
    }

    private fun isPublicClassDeclaration(text: String): Boolean {
        val firstPrefix = "public class "
        val secondPrefix = "class "
        if (!text.startsWith(firstPrefix) && !text.startsWith(secondPrefix))
            return false
        return text.removePrefix(firstPrefix)
            .removePrefix(secondPrefix)
            .split(' ')
            .first()
            .takeIf(String::isNotBlank)
            ?.all(Char::isLetter)
            ?: false
    }

    /** Returns the path of this sample source. */
    override fun toString(): String = this.file.toString()

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns the specified [file] as a Kotlin sample source, or returns
         * `null` if the [file]'s extension is other than `kt`, if the [file] is
         * not located in a test Kotlin source set, or if the [file]'s name is
         * not suffixed by `Sample`.
         */
        fun orNull(file: File): KotlinSampleSource? {
            val fileIsValid: Boolean = file.extension == "kt"
                    && file.path.contains("test/kotlin/", ignoreCase = true)
                    && file.nameWithoutExtension.endsWith("Sample")
            return if (fileIsValid) KotlinSampleSource(file) else null
        }
    }
}
