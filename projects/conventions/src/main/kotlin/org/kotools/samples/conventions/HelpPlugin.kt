package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

/** Project plugin that provides additional help tasks. */
public class HelpPlugin : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit =
        project.tasks.register("coordinates").configure {
            this.description = "Prints coordinates (group, artifact and " +
                    "version) of this project."
            this.group = "help"
            this.doLast {
                println("${project.group}:${project.name}:${project.version}")
            }
        }
}
