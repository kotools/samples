package org.kotools.samples.internal

import java.io.File

/**
 * Returns `true` if this file's name is suffixed by `Sample`, or returns
 * `false` otherwise.
 */
internal fun File.isSample(): Boolean =
    this.nameWithoutExtension.endsWith("Sample")

/**
 * Returns `true` if this file's extension is `kt`, or returns `false`
 * otherwise.
 */
internal fun File.isKotlin(): Boolean = this.extension == "kt"
