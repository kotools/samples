package org.kotools.samples.gradle.conventions

import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.diagnostics.TaskReportTask
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register
import org.gradle.testfixtures.ProjectBuilder
import org.kotools.samples.gradle.conventions.internal.TaskGroup
import kotlin.test.Test
import kotlin.test.assertEquals

class TasksPluginTest {
    @Test
    fun `configures tasks of type TaskReportTask`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        val tasks: TaskProvider<TaskReportTask> =
            project.tasks.register<TaskReportTask>("tasks")
        // When
        project.pluginManager.apply(TasksPlugin::class)
        // Then
        val actual: String = tasks.get()
            .displayGroup
        val expected: String = TaskGroup.Module.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `configures assemble lifecycle task`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply(BasePlugin::class)
        // When
        project.pluginManager.apply(TasksPlugin::class)
        // Then
        val actual: String? = project.tasks.named("assemble")
            .get()
            .group
        val expected: String = TaskGroup.Module.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `configures check lifecycle task`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply(BasePlugin::class)
        // When
        project.pluginManager.apply(TasksPlugin::class)
        // Then
        val actual: String? = project.tasks.named("check")
            .get()
            .group
        val expected: String = TaskGroup.Module.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `configures build lifecycle task`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply(BasePlugin::class)
        // When
        project.pluginManager.apply(TasksPlugin::class)
        // Then
        val actual: String? = project.tasks.named("build")
            .get()
            .group
        val expected: String = TaskGroup.Module.toString()
        assertEquals(expected, actual)
    }
}
