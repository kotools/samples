package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Java](https://www.java.com).
 *
 * @constructor Returns a Java sample source with the specified [file], or
 * throws an [IllegalArgumentException] if the [file] has another extension than
 * `java` or a name that is not suffixed by `Sample`.
 */
@JvmInline
internal value class JavaSampleSource(
    /** The file of this Java sample source. */
    val file: File
) {
    init {
        require(this.file.extension == "java") {
            "Java sample source must have 'java' file extension (input: " +
                    "${this.file})."
        }
        require(this.file.nameWithoutExtension.endsWith("Sample")) {
            "Java sample source must have 'Sample' suffix in its file name " +
                    "(input: $file)."
        }
    }

    override fun toString(): String = "'${this.file}' Java sample source"

    /** Contains static declarations for the [JavaSampleSource] type. */
    companion object {
        /**
         * Returns a Java sample source with the specified [file], or throws an
         * [IllegalArgumentException] if the [file] has another extension than
         * `java` or a name that is not suffixed by `Sample`.
         */
        fun orThrow(file: File): JavaSampleSource {
            require(file.extension == "java") {
                "Java sample source must have 'java' file extension (input: " +
                        "$file)."
            }
            require(file.nameWithoutExtension.endsWith("Sample")) {
                "Java sample source must have 'Sample' suffix in its file " +
                        "name (input: $file)."
            }
            return JavaSampleSource(file)
        }
    }
}
