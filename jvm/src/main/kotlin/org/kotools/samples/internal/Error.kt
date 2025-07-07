package org.kotools.samples.internal

import java.io.File

internal object Error {
    fun multipleClassesFoundIn(file: File): IllegalStateException =
        IllegalStateException("Multiple classes found in '$file'.")

    fun noPublicClassFoundIn(file: File): IllegalStateException =
        IllegalStateException("No public class found in '$file'.")

    fun singleExpressionKotlinFunctionFoundIn(
        file: File
    ): IllegalStateException = IllegalStateException(
        "Single-expression Kotlin function found in '$file'."
    )
}
