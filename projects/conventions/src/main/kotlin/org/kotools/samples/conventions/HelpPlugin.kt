package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.kotools.samples.conventions.tasks.Print

/** Project plugin that provides additional help tasks. */
public class HelpPlugin : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit = project.tasks
        .register<Print>("coordinates")
        .configure {
            this.description =
                "Prints coordinates (group, artifact and version)."
            this.group = "help"

            this.message.set(
                "${project.group}:${project.name}:${project.version}"
            )
        }
}
