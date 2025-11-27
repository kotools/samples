package org.kotools.samples.gradle.conventions.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
 * Task that prints the detected compatibilities of a project to the standard
 * output.
 */
@DisableCachingByDefault(because = "Prints to standard output.")
public abstract class Compatibilities internal constructor() : DefaultTask() {
    /** File containing the Java compatibility to print. */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val javaCompatibility: RegularFileProperty

    /** File containing the Kotlin compatibility to print. */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    public abstract val kotlinCompatibility: RegularFileProperty

    @TaskAction
    internal fun execute(): Unit =
        sequenceOf(this.javaCompatibility, this.kotlinCompatibility)
            .map(RegularFileProperty::get)
            .map { it.asFile }
            .map(File::readText)
            .forEach(::println)
}
