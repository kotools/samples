package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Kotlin](https://kotlinlang.org).
 *
 * For creating an instance of this type, see the methods provided by the
 * [KotlinSampleSource.Companion] type.
 */
internal class KotlinSampleSource private constructor(private val file: File) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is a Kotlin sample source
     * corresponding to the same file as this one, or returns `false` otherwise.
     *
     * See the [toFile] method for accessing the file corresponding to this
     * Kotlin sample source.
     */
    override fun equals(other: Any?): Boolean =
        other is KotlinSampleSource && other.file == this.file

    /**
     * Returns a hash code value for this Kotlin sample source.
     *
     * The returned hash code value is computed from the hash code value of the
     * file corresponding to this Kotlin sample source. See the [toFile] method
     * for accessing this file, and the [File.hashCode] method for more details
     * about its hash code value.
     */
    override fun hashCode(): Int = this.file.hashCode()

    // ----------------------- File's content operations -----------------------

    /**
     * Returns the package identifier declared in this Kotlin sample source, or
     * returns `null` if not found.
     */
    fun packageIdentifierOrNull(): PackageIdentifier? {
        val regex = Regex("""^package [a-z]+(?:\.[a-z]+)*;?$""")
        return this.file.useLines { it.firstOrNull(regex::matches) }
            ?.substringAfter("package ")
            ?.substringBefore(';')
            ?.let(PackageIdentifier.Companion::orThrow)
    }

    /**
     * Returns the name of the public class declared in this Kotlin sample
     * source.
     */
    fun className(): ClassName {
        val publicClassDeclaration: String = this.file.useLines {
            it.map(String::trim)
                .first(String::isKotlinPublicClass)
        }
        return publicClassDeclaration.substringAfter("class ")
            .substringBefore(" {")
            .let(ClassName.Companion::orThrow)
    }

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Kotlin sample source. */
    override fun toString(): String = "'${this.file.name}' Kotlin sample source"

    /** Returns the file corresponding to this Kotlin sample source. */
    fun toFile(): File = this.file

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [KotlinSampleSource] type. */
    companion object {
        /**
         * Returns a Kotlin sample source from the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample`, or if the
         * [file] has another extension than `kt`.
         *
         * See the [orThrow] method for throwing an exception instead of
         * returning `null` in case of invalid [file].
         */
        fun orNull(file: File): KotlinSampleSource? = file
            .takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.takeIf { it.extension == "kt" }
            ?.let(::KotlinSampleSource)

        /**
         * Returns a Kotlin sample source from the specified [file], or throws
         * an [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample`, or if the [file] has another extension than `kt`.
         *
         * See the [orNull] method for returning `null` instead of throwing an
         * exception in case of invalid [file].
         */
        fun orThrow(file: File): KotlinSampleSource {
            val fileName: String = file.nameWithoutExtension
            require(fileName.endsWith("Sample")) {
                "Kotlin sample source file name must end with 'Sample' " +
                        "(input: '$fileName')."
            }
            val fileExtension: String = file.extension
            require(fileExtension == "kt") {
                "Kotlin sample source file extension must be 'kt' (input: " +
                        "'$fileExtension')."
            }
            return KotlinSampleSource(file)
        }
    }
}
