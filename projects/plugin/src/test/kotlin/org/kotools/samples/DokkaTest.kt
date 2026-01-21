package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.DokkaPlugin
import kotlin.test.Test
import kotlin.test.assertEquals

class DokkaTest {
    @Test
    fun `updates main Dokka source set directory`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply(DokkaPlugin::class)

        // When
        project.pluginManager.apply(KotoolsSamplesPlugin::class)

        // Then
        val expected: String = project.tasks
            .named<InlineSamplesTask>("inlineSamples")
            .get()
            .outputDirectory
            .asFile
            .get()
            .path
        project.tasks.withType<AbstractDokkaLeafTask>()
            .forEach {
                val actual: String = it.dokkaSourceSets
                    .named(SourceSet.MAIN_SOURCE_SET_NAME)
                    .get()
                    .sourceRoots
                    .asPath
                assertEquals(expected, actual)
            }
    }
}
