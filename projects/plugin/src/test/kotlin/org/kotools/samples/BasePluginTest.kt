package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertTrue

class BasePluginTest {
    @Test
    fun `'check' task depends on 'CheckSampleReferences' task type`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        // When
        project.pluginManager.apply(KotoolsSamplesPlugin::class)

        // Then
        project.tasks.named("check")
            .get()
            .dependsOn
            .filterIsInstance<TaskProvider<CheckSampleReferencesTask>>()
            .any()
            .let(::assertTrue)
    }
}
