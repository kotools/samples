package org.kotools.samples

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
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
import java.io.File
import java.util.Objects
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

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
        val project: Project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val sample: KotlinSourceSet? = project.extensions
            .getByType<KotlinJvmProjectExtension>()
            .sourceSets
            .findByName("sample")
        val plugin = KotoolsSamplesJvmPlugin()
        assertNotNull(
            actual = sample,
            message = "$plugin should create 'sample' Kotlin source set."
        )
        val kotlinSample: Directory =
            project.layout.projectDirectory.dir("src/sample/kotlin")
        assertTrue(
            actual = kotlinSample.asFile in sample.kotlin.sourceDirectories,
            message = "Kotlin sample directory should be included in ${sample}."
        )
        val javaSample: Directory =
            project.layout.projectDirectory.dir("src/sample/java")
        assertTrue(
            actual = javaSample.asFile in sample.kotlin.sourceDirectories,
            message = "Java sample directory should be included in ${sample}."
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
        val task: CheckSampleSources = project.assertTask()
        task.assertDescription("Checks the content of sample sources.")
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = task.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
    }

    @Test
    fun `apply should create 'extractSamples' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val task: ExtractSamples = project.assertTask()
        task.assertDescription("Extracts samples for KDoc.")
        val checkSampleSources: TaskProvider<CheckSampleSources> =
            project.tasks.named<CheckSampleSources>("checkSampleSources")
        val expectedDependencies: List<TaskProvider<CheckSampleSources>> =
            listOf(checkSampleSources)
        val actualDependencies: List<Any> = task.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = task.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
        val expectedOutputDirectory: Directory = project.layout.buildDirectory
            .dir("samples/extracted")
            .get()
        val actualOutputDirectory: Directory = task.outputDirectory.get()
        assertEquals(expectedOutputDirectory, actualOutputDirectory)
    }

    @Test
    fun `apply should create 'checkSampleReferences' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val task: CheckSampleReferences = project.assertTask()
        task.assertDescription("Checks sample references from KDoc.")
        val extractSamples: TaskProvider<ExtractSamples> =
            project.tasks.named<ExtractSamples>("extractSamples")
        val expectedDependencies: List<TaskProvider<ExtractSamples>> =
            listOf(extractSamples)
        val actualDependencies: List<Any> = task.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val expectedSourceDirectory: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSourceDirectory: Directory = task.sourceDirectory.get()
        assertEquals(expectedSourceDirectory, actualSourceDirectory)
        val expectedExtractedSamplesDirectory: Directory = extractSamples
            .flatMap(ExtractSamples::outputDirectory)
            .get()
        val actualExtractedSamplesDirectory: Directory =
            task.extractedSamplesDirectory.get()
        assertEquals(
            expectedExtractedSamplesDirectory,
            actualExtractedSamplesDirectory
        )
    }

    @Test
    fun `apply should create 'backupMainSources' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val task: Copy = project.assertTask("backupMainSources")
        task.assertDescription("Copies main sources into the build directory.")
        val checkSampleReferences: TaskProvider<CheckSampleReferences> =
            project.tasks.named<CheckSampleReferences>("checkSampleReferences")
        val expectedDependencies: List<TaskProvider<CheckSampleReferences>> =
            listOf(checkSampleReferences)
        val actualDependencies: List<Any> = task.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val source: Directory = project.layout.projectDirectory.dir("src")
        val areSourceFilesInSourceDirectory: Boolean =
            task.source.all { source.asFile.path in it.path }
        assertTrue(
            actual = areSourceFilesInSourceDirectory,
            message = "Source directory should be ${source}."
        )
        val areApiFilesExcluded: Boolean =
            task.source.files.none { "api" in it.path }
        assertTrue(
            actual = areApiFilesExcluded,
            message = "API files should be excluded from source directory."
        )
        val areSamplesExcluded: Boolean =
            task.source.files.none { "sample" in it.path }
        assertTrue(
            actual = areSamplesExcluded,
            message = "Samples should be excluded from source directory."
        )
        val areTestsExcluded: Boolean =
            task.source.files.none { "test" in it.path }
        assertTrue(
            actual = areTestsExcluded,
            message = "Tests should be excluded from source directory."
        )
        val expectedDestinationDirectory: File = project.layout.buildDirectory
            .dir("samples/sources-backup")
            .get()
            .asFile
        val actualDestinationDirectory: File = task.destinationDir
        assertEquals(expectedDestinationDirectory, actualDestinationDirectory)
    }

    @Test
    fun `apply should create 'inlineSamples' task`() {
        val project = Project()
            .applyKotlinAndKotoolsSamplesJvmPlugins()
        val task: InlineSamples = project.assertTask()
        task.assertDescription("Inlines KDoc samples.")
        val backupMainSources: TaskProvider<Copy> =
            project.tasks.named<Copy>("backupMainSources")
        val expectedDependencies: List<TaskProvider<Copy>> =
            listOf(backupMainSources)
        val actualDependencies: List<Any> = task.dependsOn.toList()
        assertContentEquals(expectedDependencies, actualDependencies)
        val expectedSource: Directory =
            project.layout.projectDirectory.dir("src")
        val actualSource: Directory = task.sourceDirectory.get()
        assertEquals(expectedSource, actualSource)
        val expectedExtractedSamples: Directory = project.tasks
            .named<ExtractSamples>("extractSamples")
            .flatMap(ExtractSamples::outputDirectory)
            .get()
        val actualExtractedSamples: Directory =
            task.extractedSamplesDirectory.get()
        assertEquals(expectedExtractedSamples, actualExtractedSamples)
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

// ---------------------------- Project extensions -----------------------------

@Suppress("TestFunctionName")
private fun Project(): Project = ProjectBuilder.builder()
    .withName("test")
    .build()

private fun Project.applyKotlinAndKotoolsSamplesJvmPlugins(): Project {
    this.pluginManager.apply(KotlinPlatformJvmPlugin::class)
    this.pluginManager.apply(KotoolsSamplesJvmPlugin::class)
    return this
}

private inline fun <reified T : Task> Project.assertTask(): T {
    val kClass: KClass<T> = T::class
    val name: String = kClass.simpleName?.replaceFirstChar(Char::lowercaseChar)
        ?: fail("The $kClass has no name.")
    return this.assertTask(name)
}

private inline fun <reified T : Task> Project.assertTask(name: String): T {
    val actual: T? = this.tasks.findByName(name) as? T
    val message = "The '$name' Gradle task is not found."
    return assertNotNull(actual, message)
}

// ------------------------------ Task extensions ------------------------------

private fun Task.assertDescription(expected: String): Unit =
    assertEquals(expected, actual = this.description)
