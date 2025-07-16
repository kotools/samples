package org.kotools.samples

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import org.kotools.samples.internal.className
import org.kotools.samples.internal.isJava
import org.kotools.samples.internal.isJavaPublicClass
import org.kotools.samples.internal.isKotlin
import org.kotools.samples.internal.isKotlinFunction
import org.kotools.samples.internal.isKotlinPublicClass
import org.kotools.samples.internal.isPackage
import org.kotools.samples.internal.isSample
import org.kotools.samples.internal.kotlinFunctionName
import org.kotools.samples.internal.packageIdentifier
import org.kotools.samples.internal.toKotlinMarkdownCodeBlock
import java.io.File

/** Gradle task responsible for extracting samples for KDoc. */
@DisableCachingByDefault(because = "Generating files doesn't worth caching.")
public abstract class ExtractSamples : DefaultTask() {
    /** The directory containing sample sources. */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val sourceDirectory: DirectoryProperty

    /** The directory that will contain the extracted KDoc samples. */
    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @TaskAction
    internal fun execute() {
        val directory: Directory = this.outputDirectory.get()
        val files: Sequence<File> = this.sourceDirectory.asFileTree.asSequence()
            .filterNotNull()
            .filter(File::isSample)
        files.filter(File::isKotlin)
            .forEach { it.saveKotlinSamplesIn(directory) }
        files.filter(File::isJava)
            .forEach { it.saveJavaSamplesIn(directory) }
    }
}

// ---------------------------------- Kotlin -----------------------------------

private fun File.saveKotlinSamplesIn(directory: Directory): Unit = this
    .kotlinFunctions()
    .mapKeys {
        val packageIdentifier: String? = this.packageIdentifierOrNull()
        val className: String = this.publicKotlinClassName()
        it.key.toMarkdownFilePath(packageIdentifier, className)
    }
    .mapValues { it.value.toKotlinMarkdownCodeBlock() }
    .forEach { it.saveAsFileIn(directory) }

private fun File.kotlinFunctions(): Map<String, String> {
    val functionNames: Set<String> = this.useLines {
        it.map(String::trim)
            .filter(String::isKotlinFunction)
            .map(String::kotlinFunctionName)
            .toSet()
    }
    return functionNames.associateWith(this::functionBody)
}

private fun File.publicKotlinClassName(): String = this
    .useLines { it.first(String::isKotlinPublicClass) }
    .className()

// ----------------------------------- Java ------------------------------------

private fun File.saveJavaSamplesIn(directory: Directory): Unit = this
    .javaFunctions()
    .mapKeys {
        val packageIdentifier: String? = this.packageIdentifierOrNull()
        val className: String = this.publicJavaClassName()
        it.key.toMarkdownFilePath(packageIdentifier, className)
    }
    .mapValues { it.value.toJavaMarkdownCodeBlock() }
    .forEach { it.saveAsFileIn(directory) }

private fun File.javaFunctions(): Map<String, String> {
    val functionNames: Set<String> = this.useLines { lines: Sequence<String> ->
        val regex = Regex("""void [A-Za-z_]+\(\) \{$""")
        lines.filter(regex::containsMatchIn)
            .map { it.substringBefore('(') }
            .map { it.substringAfter("void ") }
            .toSet()
    }
    return functionNames.associateWith(this::functionBody)
}

private fun File.publicJavaClassName(): String = this
    .useLines { it.first(String::isJavaPublicClass) }
    .className()

private fun String.toJavaMarkdownCodeBlock(): String = """
    |```java
    |$this
    |```
""".trimMargin()

// ------------------------------- Kotlin & Java -------------------------------

private fun File.packageIdentifierOrNull(): String? = this
    .useLines { it.firstOrNull(String::isPackage) }
    ?.packageIdentifier()

private fun File.functionBody(name: String): String {
    val body: List<String> = this.useLines { lines: Sequence<String> ->
        val header = "$name() {"
        var unclosedBracketCount = 0
        var read = false
        lines.filter { header in it || read }
            .onEach { if ('{' in it) unclosedBracketCount++ }
            .onEach { if ('}' in it) unclosedBracketCount-- }
            .onEach {
                if (!read && unclosedBracketCount > 0) read = true
                else if (read && unclosedBracketCount == 0) read = false
            }
            .filter { header !in it && read }
            .toList()
    }
    return body.joinToString(separator = "\n")
        .trimIndent()
}

private fun String.toMarkdownFilePath(
    packageIdentifier: String?,
    className: String
): String {
    val suffix = "$className/${this}.md"
    return packageIdentifier?.replace(oldChar = '.', newChar = '/')
        ?.plus("/$suffix")
        ?: suffix
}

private fun Map.Entry<String, String>.saveAsFileIn(directory: Directory): Unit =
    directory.file(this.key)
        .asFile
        .also(File::ensureParentDirsCreated)
        .writeText(this.value)
