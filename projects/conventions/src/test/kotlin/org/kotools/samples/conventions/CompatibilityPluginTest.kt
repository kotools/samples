package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CompatibilityPluginTest {
    @Test
    fun `creates compatibility extension`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        // When
        project.pluginManager.apply(CompatibilityPlugin::class)
        // Then
        val compatibility: CompatibilityExtension? = project.extensions
            .findByName("compatibility") as? CompatibilityExtension
        assertNotNull(compatibility, "Compatibility extension not found.")
    }

    @Test
    fun `configures Java compatibility for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityExtension =
            project.extensions.getByType()
        val javaVersion = "17"
        // When
        compatibility.java.set(javaVersion)
        // Then
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val actualJvmTarget: JvmTarget = kotlin.compilerOptions.jvmTarget.get()
        assertEquals(expected = JvmTarget.JVM_17, actualJvmTarget)
        val compilerArguments: List<String> = kotlin.compilerOptions
            .freeCompilerArgs
            .get()
            .filterNotNull()
        assertTrue("-Xjdk-release=$javaVersion" in compilerArguments)
        val javaReleaseIsSet: Boolean = project.tasks.withType<JavaCompile>()
            .map { it.options.release }
            .all { it.get() == javaVersion.toInt() }
        assertTrue(javaReleaseIsSet)
    }

    @Test
    fun `configures Kotlin compatibility for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityExtension =
            project.extensions.getByType()
        val kotlinVersion = "2.0.21"
        // When
        compatibility.kotlin.set(kotlinVersion)
        // Then
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val actualApiVersion: KotlinVersion =
            kotlin.compilerOptions.apiVersion.get()
        val expectedKotlinVersion: KotlinVersion = KotlinVersion.KOTLIN_2_0
        assertEquals(expectedKotlinVersion, actualApiVersion)
        val actualLanguageVersion: KotlinVersion =
            kotlin.compilerOptions.languageVersion.get()
        assertEquals(expectedKotlinVersion, actualLanguageVersion)
        assertEquals(kotlinVersion, kotlin.coreLibrariesVersion)
    }
}
