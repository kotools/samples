package org.kotools.samples.internal

import java.io.File

/**
 * Represents a sample source written in [Java](https://www.java.com).
 *
 * For creating an instance of this type, see the methods provided by the
 * [JavaSampleSource.Companion] type.
 */
internal class JavaSampleSource private constructor() {
    /** Contains static declarations for the [JavaSampleSource] type. */
    companion object {
        /**
         * Returns a Java sample source from the specified [file], or returns
         * `null` if the [file]'s name is not suffixed by `Sample`, or if the
         * [file] has another extension than `java`.
         */
        fun orNull(file: File): JavaSampleSource? = file
            .takeIf { it.nameWithoutExtension.endsWith("Sample") }
            ?.takeIf { it.extension == "java" }
            ?.let { JavaSampleSource() }
    }
}
