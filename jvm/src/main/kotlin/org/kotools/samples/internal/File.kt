package org.kotools.samples.internal

import java.io.File

internal fun File.isSample(): Boolean =
    this.nameWithoutExtension.endsWith("Sample")

internal fun File.isKotlin(): Boolean = this.extension == "kt"
