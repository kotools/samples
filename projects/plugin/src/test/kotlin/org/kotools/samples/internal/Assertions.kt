package org.kotools.samples.internal

import org.gradle.testkit.runner.BuildResult
import kotlin.test.assertTrue

internal fun BuildResult.assertOutputContains(expected: String): Unit =
    assertTrue(
        actual = expected in this.output,
        message = "Expected <$expected> in output, but was:\n${this.output}"
    )
