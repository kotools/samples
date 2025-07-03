@file:JvmName("FileExtensions")

package org.kotools.samples.internal

import java.io.File

/**
 * Returns `true` if this file has a name suffixed by `Sample`, excluding its
 * file extension, or returns `false` otherwise.
 *
 * See the [File.nameWithoutExtension] property for accessing this file's name
 * without its file extension.
 */
internal fun File.isSampleSource(): Boolean =
    this.nameWithoutExtension.endsWith("Sample")

/**
 * Returns `true` if this file's extension is `kt`, or returns `false`
 * otherwise.
 *
 * See the [File.extension] property for accessing this file's extension.
 */
internal fun File.isKotlin(): Boolean = this.extension == "kt"
