package org.kotools.samples.conventions

import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.kotools.samples.conventions.tasks.JavaCompatibility
import org.kotools.samples.conventions.tasks.KotlinCompatibility
import kotlin.test.Test
import kotlin.test.assertEquals
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
        project.extensions.getByName<CompatibilityPluginExtension>(
            "compatibility"
        )
    }

    @Test
    fun `configures Java compatibility for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityPluginExtension =
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
        val compatibility: CompatibilityPluginExtension =
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

    @Test
    fun `registers javaCompatibility task for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        // When
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityPluginExtension =
            project.extensions.getByType()
        compatibility.java.set("17")
        // Then
        val task: JavaCompatibility = project.tasks
            .named<JavaCompatibility>("javaCompatibility")
            .get()
        assertEquals("Prints Java compatibility.", task.description)
        assertEquals("compatibility", task.group)
        assertEquals(compatibility.java.get(), task.compatibilityVersion.get())
        val compileJava: JavaCompile = project.tasks
            .named<JavaCompile>("compileJava")
            .get()
        assertEquals(compileJava.sourceCompatibility, task.sourceVersion.get())
        assertEquals(compileJava.targetCompatibility, task.targetVersion.get())
    }

    @Test
    fun `registers kotlinCompatibility task for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        // When
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityPluginExtension =
            project.extensions.getByType()
        compatibility.kotlin.set("2.0.21")
        // Then
        val task: KotlinCompatibility = project.tasks
            .named<KotlinCompatibility>("kotlinCompatibility")
            .get()
        assertEquals("Prints Kotlin compatibility.", task.description)
        assertEquals("compatibility", task.group)
        assertEquals(
            compatibility.kotlin.get(),
            task.compatibilityVersion.get()
        )
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        assertEquals(
            kotlin.compilerOptions.apiVersion.get(),
            task.apiVersion.get()
        )
        assertEquals(
            kotlin.compilerOptions.languageVersion.get(),
            task.languageVersion.get()
        )
        assertEquals(
            kotlin.coreLibrariesVersion,
            task.coreLibrariesVersion.get()
        )
    }
}
