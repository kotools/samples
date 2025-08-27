package org.kotools.samples.internal

import java.io.File

// ---------------------------------- Checks -----------------------------------

internal fun File.isSample(): Boolean =
    this.nameWithoutExtension.endsWith("Sample")

internal fun File.isKotlin(): Boolean = this.extension == "kt"

internal fun File.isJava(): Boolean = this.extension == "java"

// ---------------------------------- Errors -----------------------------------

internal fun File.multipleClassesFound(): IllegalStateException =
    IllegalStateException("Multiple classes found in '$this'.")

internal fun File.noPublicClassFound(): IllegalStateException =
    IllegalStateException("No public class found in '$this'.")
