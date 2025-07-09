package org.kotools.samples.internal

import java.io.File

internal fun File.multipleClassesFound(): IllegalStateException =
    IllegalStateException("Multiple classes found in '$this'.")

internal fun File.noPublicClassFound(): IllegalStateException =
    IllegalStateException("No public class found in '$this'.")

internal fun File.singleExpressionKotlinFunctionFound(): IllegalStateException =
    IllegalStateException("Single-expression Kotlin function found in '$this'.")
