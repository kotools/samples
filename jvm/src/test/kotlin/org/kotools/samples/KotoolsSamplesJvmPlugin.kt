package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.kotools.samples.internal.KotlinJvmPluginNotFound
import java.util.Objects
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KotoolsSamplesJvmPluginTest {
    // -------------------- Structural equality operations ---------------------

    @Test
    fun `equals should pass with a KotoolsSamplesJvmPlugin type`() {
        val plugin = KotoolsSamplesJvmPlugin()
        val other: Any = KotoolsSamplesJvmPlugin()
        val actual: Boolean = plugin == other
        val message = "Gradle plugins with the same type should be equal."
        assertTrue(actual, message)
    }

    @Test
    fun `equals should fail with another type than KotoolsSamplesJvmPlugin`() {
        val plugin = KotoolsSamplesJvmPlugin()
        val other: Any = "$plugin"
        val actual: Boolean = plugin == other
        val message = "Gradle plugins with different types shouldn't be equal."
        assertFalse(actual, message)
    }

    @Test
    fun `hashCode should pass`() {
        val plugin = KotoolsSamplesJvmPlugin()
        val actual: Int = plugin.hashCode()
        val expected: Int = Objects.hash("$plugin")
        assertEquals(expected, actual)
    }

    // ------------------------- Project configuration -------------------------

    @Test
    fun `apply should fail if Kotlin JVM plugin wasn't applied to project`() {
        val project = Project()
        val exception: PluginApplicationException = assertFailsWith {
            project.pluginManager.apply(KotoolsSamplesJvmPlugin::class)
        }
        val actual: String? = exception.cause?.message
        val expected: String = KotlinJvmPluginNotFound(project)
            .toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `apply should create 'sample' Kotlin source set`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val sample: KotlinSourceSet? = kotlin.sourceSets.findByName("sample")
        val plugin = KotoolsSamplesJvmPlugin()
        assertNotNull(
            actual = sample,
            message = "$plugin should create 'sample' source set"
        )
        val sampleSources: Directory =
            project.layout.projectDirectory.dir("src/sample")
        val kotlinSamples: Directory = sampleSources.dir("kotlin")
        assertTrue(
            actual = kotlinSamples.asFile in sample.kotlin.sourceDirectories,
            message = "Kotlin sample directory should be included in $sample"
        )
        val javaSamples: Directory = sampleSources.dir("java")
        assertTrue(
            actual = javaSamples.asFile in sample.kotlin.sourceDirectories,
            message = "Java sample directory should be included in $sample"
        )
    }

    @Test
    fun `apply should configure 'main' Kotlin source set`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val main: KotlinSourceSet = kotlin.sourceSets.getByName("main")
        val sample: KotlinSourceSet = kotlin.sourceSets.getByName("sample")
        val actual: Boolean = main in sample.dependsOn
        val message = "$sample should depend on $main"
        assertTrue(actual, message)
    }

    @Test
    fun `apply should configure 'test' Kotlin source set`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val kotlin: KotlinJvmProjectExtension = project.extensions.getByType()
        val sample: KotlinSourceSet = kotlin.sourceSets.getByName("sample")
        val test: KotlinSourceSet = kotlin.sourceSets.getByName("test")
        val actual: Boolean = sample in test.dependsOn
        val message = "$test should depend on $sample"
        assertTrue(actual, message)
    }

    @Test
    fun `apply should configure 'test' Java source set`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val test: SourceSet = project.extensions
            .getByType<JavaPluginExtension>()
            .sourceSets
            .getByName("test")
        val sourceDirectories: FileCollection = test.java.sourceDirectories
        val javaSample: Directory =
            project.layout.projectDirectory.dir("src/sample/java")
        val actual: Boolean = javaSample.asFile in sourceDirectories
        val message = "Java sample directory should be included in ${test}."
        assertTrue(actual, message)
    }

    @Test
    fun `apply should create 'checkSampleSources' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val taskName = "checkSampleSources"
        val actual: CheckSampleSources? =
            project.tasks.findByName(taskName) as? CheckSampleSources
        assertNotNull(actual, "The '$taskName' Gradle task is not found.")
        val expectedDescription = "Checks the content of sample sources."
        assertEquals(expectedDescription, actual.description)
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = actual.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
    }

    @Test
    fun `apply should create 'extractSamples' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val taskName = "extractSamples"
        val actual: ExtractSamples? =
            project.tasks.findByName(taskName) as? ExtractSamples
        assertNotNull(actual, "The '$taskName' Gradle task is not found.")
        val expectedDescription = "Extracts samples for KDoc."
        assertEquals(expectedDescription, actual.description)
        val checkSampleSources: TaskProvider<Task> =
            project.tasks.named("checkSampleSources")
        val expectedDependencies: List<TaskProvider<Task>> =
            listOf(checkSampleSources)
        val actualDependencies: List<Any> = actual.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = actual.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
        val expectedOutputDirectory: Directory = project.layout.buildDirectory
            .dir("samples/extracted")
            .get()
        val actualOutputDirectory: Directory = actual.outputDirectory.get()
        assertEquals(expectedOutputDirectory, actualOutputDirectory)
    }

    @Test
    fun `apply should create 'checkSampleReferences' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val taskName = "checkSampleReferences"
        val actual: CheckSampleReferences? =
            project.tasks.findByName(taskName) as? CheckSampleReferences
        assertNotNull(actual, "The '$taskName' Gradle task is not found.")
        val expectedDescription = "Checks sample references from KDoc."
        assertEquals(expectedDescription, actual.description)
        val extractSamples: TaskProvider<ExtractSamples> =
            project.tasks.named<ExtractSamples>("extractSamples")
        val expectedDependencies: List<TaskProvider<ExtractSamples>> =
            listOf(extractSamples)
        val actualDependencies: List<Any> = actual.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = actual.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
        val expectedExtractedSamplesDirectory: Directory = extractSamples
            .flatMap(ExtractSamples::outputDirectory)
            .get()
        val actualExtractedSamplesDirectory: Directory =
            actual.extractedSamplesDirectory.get()
        assertEquals(
            expectedExtractedSamplesDirectory,
            actualExtractedSamplesDirectory
        )
    }

    // ------------------------------ Conversions ------------------------------

    @Test
    fun `toString should pass`() {
        val actual: String = KotoolsSamplesJvmPlugin()
            .toString()
        val expected = "Kotools Samples Gradle plugin for Kotlin/JVM projects"
        assertEquals(expected, actual)
    }
}

@Suppress("TestFunctionName")
private fun Project(): Project = ProjectBuilder.builder()
    .withName("test")
    .build()

private fun Project.applyKotlinAndKotoolsSamplesJvmPlugins(): Project {
    this.pluginManager.apply(KotlinPlatformJvmPlugin::class)
    this.pluginManager.apply(KotoolsSamplesJvmPlugin::class)
    return this
}
