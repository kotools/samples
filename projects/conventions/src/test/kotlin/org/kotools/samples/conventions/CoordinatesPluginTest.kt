package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesPluginTest {
    @Test
    fun `registers coordinates task`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()

        // When
        project.pluginManager.apply(CoordinatesPlugin::class)

        // Then
        val task: CoordinatesTask = project.tasks
            .named<CoordinatesTask>("coordinates")
            .get()
        val actualDescription: String = task.description
        val expectedDescription = "Prints coordinates (group:name:version)."
        assertEquals(expectedDescription, actualDescription)

        val actualGroup: String = task.group
        val expectedGroup = "help"
        assertEquals(expectedGroup, actualGroup)

        val actualProjectGroup: String = task.projectGroup.get()
        val expectedProjectGroup: String = project.group.toString()
        assertEquals(expectedProjectGroup, actualProjectGroup)

        val actualProjectName: String = task.projectName.get()
        val expectedProjectName: String = project.name
        assertEquals(expectedProjectName, actualProjectName)

        val actualProjectVersion: String = task.projectVersion.get()
        val expectedProjectVersion: String = project.version.toString()
        assertEquals(expectedProjectVersion, actualProjectVersion)
    }
}
