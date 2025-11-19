package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class HelpPluginTest {
    @Test
    fun `apply registers coordinates task`() {
        // Given
        val plugin = HelpPlugin()
        val project: Project = ProjectBuilder.builder()
            .build()
        // When
        plugin.apply(project)
        // Then
        val coordinates: Task? = project.tasks.findByName("coordinates")
        assertNotNull(coordinates, "Coordinates task not found.")
        val expectedDescription =
            "Prints coordinates (group, artifact and version) of this project."
        assertEquals(expectedDescription, coordinates.description)
        assertEquals(expected = "help", coordinates.group)
    }
}
