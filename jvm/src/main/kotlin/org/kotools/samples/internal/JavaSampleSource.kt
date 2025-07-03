package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Java](https://www.java.com).
 *
 * For creating an instance of this type, see the methods provided by the
 * [JavaSampleSource.Companion] type.
 */
internal class JavaSampleSource private constructor(private val file: File) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is a Java sample source
     * corresponding to the same file as this one, or returns `false` otherwise.
     *
     * See the [toFile] method for accessing the file corresponding to this Java
     * sample source.
     */
    override fun equals(other: Any?): Boolean =
        other is JavaSampleSource && other.file == this.file

    /**
     * Returns a hash code value for this Java sample source.
     *
     * The returned hash code value is computed from the hash code value of the
     * file corresponding to this Java sample source. See the [toFile] method
     * for accessing this file, and the [File.hashCode] method for more details
     * about its hash code value.
     */
    override fun hashCode(): Int = this.file.hashCode()

    // ----------------------- File's content operations -----------------------

    /**
     * Returns the message of the first exception found in the file
     * corresponding to this Java sample source, or returns `null` if no content
     * exception was found.
     *
     * See the [toFile] method for accessing the file corresponding to this Java
     * sample source.
     */
    fun contentExceptionOrNull(): ExceptionMessage? {
        val classDeclarations: List<String> = this.file.useLines {
            val regex = Regex("""class [A-Z][A-Za-z]* \{""")
            it.map(String::trim)
                .filter(regex::containsMatchIn)
                .toList()
        }
        if (classDeclarations.count() > 1)
            return ExceptionMessage.multipleClassesFoundIn(this.file)
        val publicClassCount: Int =
            classDeclarations.count { it.startsWith("public class ") }
        return if (publicClassCount == 1) null
        else ExceptionMessage.noPublicClassFoundIn(this.file)
    }

    /**
     * Returns the package identifier declared in this Java sample source, or
     * returns `null` if not found.
     */
    fun packageIdentifierOrNull(): PackageIdentifier? {
        val regex = Regex("""^package [a-z]+(?:\.[a-z]+)*;$""")
        return this.file.useLines { it.firstOrNull(regex::matches) }
            ?.substringAfter("package ")
            ?.substringBefore(';')
            ?.let(PackageIdentifier.Companion::orThrow)
    }

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this Java sample source. */
    override fun toString(): String = "'${this.file.name}' Java sample source"

    /** Returns the file corresponding to this Java sample source. */
    fun toFile(): File = this.file

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [JavaSampleSource] type. */
    companion object {
        /**
         * Returns a Java sample source from the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample`, or if the
         * [file] has another extension than `java`.
         *
         * See the [orThrow] method for throwing an exception instead of
         * returning `null` in case of invalid [file].
         */
        fun orNull(file: File): JavaSampleSource? = file
            .takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.takeIf { it.extension == "java" }
            ?.let(::JavaSampleSource)

        /**
         * Returns a Java sample source from the specified [file], or throws an
         * [IllegalArgumentException] if the [file]'s name is not suffixed by
         * `Sample`, or if the [file] has another extension than `java`.
         *
         * See the [orNull] method for returning `null` instead of throwing an
         * exception in case of invalid [file].
         */
        fun orThrow(file: File): JavaSampleSource {
            val fileName: String = file.nameWithoutExtension
            require(fileName.endsWith("Sample")) {
                "Java sample source file name must end with 'Sample' (input: " +
                        "'$fileName')."
            }
            val fileExtension: String = file.extension
            require(fileExtension == "java") {
                "Java sample source file extension must be 'java' (input: " +
                        "'$fileExtension')."
            }
            return JavaSampleSource(file)
        }
    }
}
