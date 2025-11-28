package org.kotools.samples.conventions

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Task that prints the coordinates of a project to the standard output.
 *
 * Project's coordinates are printed with the following structure:
 * `group:name:version`. Its components are respectively set with
 * [projectGroup], [projectName] and [projectVersion] properties.
 */
@DisableCachingByDefault(because = "Prints to standard output.")
public abstract class CoordinatesTask internal constructor() : DefaultTask() {
    /** Project's group to print. */
    @get:Input
    public abstract val projectGroup: Property<String>

    /** Project's name to print. */
    @get:Input
    public abstract val projectName: Property<String>

    /** Project's version to print. */
    @get:Input
    public abstract val projectVersion: Property<String>

    @TaskAction
    internal fun execute() {
        val group: String = projectGroup.get()
        val name: String = projectName.get()
        val version: String = projectVersion.get()
        println("$group:$name:$version")
    }
}
