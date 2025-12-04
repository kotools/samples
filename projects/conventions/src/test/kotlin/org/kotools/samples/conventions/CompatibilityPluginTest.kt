package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class CompatibilityPluginTest {
    @Test
    fun `creates compatibility plugin extension`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()

        // When
        project.pluginManager.apply(CompatibilityPlugin::class)

        // Then
        project.extensions.getByName<CompatibilityPluginExtension>(
            "compatibility"
        )
    }
}
