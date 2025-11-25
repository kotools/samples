package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.api.internal.tasks.testing.TestFramework
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.gradle.plugin.devel.tasks.ValidatePlugins
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.utils.named
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotlinDslPluginTest {
    @Test
    fun `configures kotlin extension`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        // When
        project.pluginManager.apply(KotlinDslPlugin::class)
        // Then
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        assertEquals(ExplicitApiMode.Strict, kotlin.explicitApi)
        assertTrue(kotlin.compilerOptions.allWarningsAsErrors.get())
    }

    @Test
    fun `configures test task of type Test`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        // When
        project.pluginManager.apply(KotlinDslPlugin::class)
        // Then
        val framework: TestFramework = project.tasks
            .named<org.gradle.api.tasks.testing.Test>("test")
            .get()
            .testFrameworkProperty
            .get()
        assertTrue(framework is JUnitPlatformTestFramework)
    }

    @Test
    fun `configures ValidatePlugins tasks`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply(JavaGradlePluginPlugin::class)
        // When
        project.pluginManager.apply(KotlinDslPlugin::class)
        // Then
        project.tasks.withType<ValidatePlugins>().forEach {
            assertTrue(it.failOnWarning.get())
            assertTrue(it.enableStricterValidation.get())
        }
    }
}
