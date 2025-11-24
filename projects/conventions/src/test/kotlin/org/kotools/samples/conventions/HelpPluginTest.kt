package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.testfixtures.ProjectBuilder
import org.kotools.samples.conventions.tasks.Print
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpPluginTest {
    @Test
    fun `registers coordinates task`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        // When
        project.pluginManager.apply(HelpPlugin::class)
        // Then
        val coordinates: Print = project.tasks.named<Print>("coordinates")
            .get()
        val expectedDescription =
            "Prints coordinates (group, artifact and version)."
        assertEquals(expectedDescription, coordinates.description)
        assertEquals(expected = "help", coordinates.group)
        val expectedMessage =
            "${project.group}:${project.name}:${project.version}"
        assertEquals(expectedMessage, coordinates.message.get())
    }
}
