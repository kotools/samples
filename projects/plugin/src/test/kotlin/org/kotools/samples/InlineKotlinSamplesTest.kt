package org.kotools.samples

import kotlin.test.Test

class InlineKotlinSamplesTest {
    @Test
    fun `inlines sample into single-line KDoc`() {
        // Given
        val project: GradleProject = GradleProject.create()
        project.sampleSource(
            """
                class IntSample {
                    fun addition(): Unit = check(1 + 2 == 3)
                }
            """.trimIndent()
        )
        project.mainSource(
            """
                /** SAMPLE: [IntSample.addition] */
                fun addition(x: Int, y: Int): Int = x + y
            """.trimIndent()
        )

        // When
        project.successfulBuild(taskPath = ":inlineKotlinSamples")

        // Then
        project.assertInlinedMainSource(
            """
                /**
                 * ```kotlin
                 * check(1 + 2 == 3)
                 * ```
                 */
                fun addition(x: Int, y: Int): Int = x + y
            """.trimIndent()
        )
    }
    // TODO: inlines sample into multi-line KDoc
    // TODO: inlines multiple samples into KDoc
    // TODO: fails when referenced sample doesn't exist
}
