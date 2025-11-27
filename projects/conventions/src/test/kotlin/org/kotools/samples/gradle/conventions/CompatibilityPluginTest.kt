package org.kotools.samples.gradle.conventions

import org.gradle.api.Project
import org.gradle.api.file.RegularFile
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
import org.kotools.samples.gradle.conventions.tasks.Compatibilities
import org.kotools.samples.gradle.conventions.tasks.JavaCompatibility
import org.kotools.samples.gradle.conventions.tasks.KotlinCompatibility
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
        assertNull(task.description)
        assertNull(task.group)

        val actualCompatibilityVersion: String = task.compatibilityVersion.get()
        val expectedCompatibilityVersion: String = compatibility.java.get()
        assertEquals(expectedCompatibilityVersion, actualCompatibilityVersion)

        val actualSourceVersion: String = task.sourceVersion.get()
        val compileJava: JavaCompile = project.tasks
            .named<JavaCompile>("compileJava")
            .get()
        val expectedSourceVersion: String = compileJava.sourceCompatibility
        assertEquals(expectedSourceVersion, actualSourceVersion)

        val actualTargetVersion: String = task.targetVersion.get()
        val expectedTargetVersion: String = compileJava.targetCompatibility
        assertEquals(expectedTargetVersion, actualTargetVersion)

        val actualOutputFile: RegularFile = task.outputFile.get()
        val expectedOutputFile: RegularFile = project.layout.buildDirectory
            .file("compatibility/java.txt")
            .get()
        assertEquals(expectedOutputFile, actualOutputFile)
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
        assertNull(task.description)
        assertNull(task.group)

        val actualCompatibilityVersion: String = task.compatibilityVersion.get()
        val expectedCompatibilityVersion: String = compatibility.kotlin.get()
        assertEquals(expectedCompatibilityVersion, actualCompatibilityVersion)

        val actualApiVersion: KotlinVersion = task.apiVersion.get()
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val expectedApiVersion: KotlinVersion =
            kotlin.compilerOptions.apiVersion.get()
        assertEquals(expectedApiVersion, actualApiVersion)

        val actualLanguageVersion: KotlinVersion = task.languageVersion.get()
        val expectedLanguageVersion: KotlinVersion =
            kotlin.compilerOptions.languageVersion.get()
        assertEquals(expectedLanguageVersion, actualLanguageVersion)

        val actualCoreLibrariesVersion: String = task.coreLibrariesVersion.get()
        val expectedCoreLibrariesVersion: String = kotlin.coreLibrariesVersion
        assertEquals(expectedCoreLibrariesVersion, actualCoreLibrariesVersion)

        val actualOutputFile: RegularFile = task.outputFile.get()
        val expectedOutputFile: RegularFile = project.layout.buildDirectory
            .file("compatibility/kotlin.txt")
            .get()
        assertEquals(expectedOutputFile, actualOutputFile)
    }

    @Test
    fun `registers compatibilities task for Kotlin JVM project`() {
        // Given
        val project: Project = ProjectBuilder.builder()
            .build()
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        // When
        project.pluginManager.apply(CompatibilityPlugin::class)
        val compatibility: CompatibilityPluginExtension =
            project.extensions.getByType()
        compatibility.java.set("17")
        compatibility.kotlin.set("2.0.21")

        // Then
        val compatibilities: Compatibilities = project.tasks
            .named<Compatibilities>("compatibilities")
            .get()
        val actualDescription: String = compatibilities.description
        val expectedDescription = "Prints detected compatibilities."
        assertEquals(expectedDescription, actualDescription)

        val actualGroup: String = compatibilities.group
        val expectedGroup = "help"
        assertEquals(expectedGroup, actualGroup)

        val actualJavaCompatibility: RegularFile =
            compatibilities.javaCompatibility.get()
        val expectedJavaCompatibility: RegularFile = project.tasks
            .named<JavaCompatibility>("javaCompatibility")
            .get()
            .outputFile
            .get()
        assertEquals(expectedJavaCompatibility, actualJavaCompatibility)

        val actualKotlinCompatibility: RegularFile =
            compatibilities.kotlinCompatibility.get()
        val expectedKotlinCompatibility: RegularFile = project.tasks
            .named<KotlinCompatibility>("kotlinCompatibility")
            .get()
            .outputFile
            .get()
        assertEquals(expectedKotlinCompatibility, actualKotlinCompatibility)
    }
}
