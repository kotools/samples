package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

/** Plugin that configure the coordinates of a project. */
public class CoordinatesPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        project.tasks.register<CoordinatesTask>("coordinates").configure {
            this.description = "Prints coordinates (group:name:version)."
            this.group = "help"

            this.projectGroup.set("${project.group}")
            this.projectName.set(project.name)
            this.projectVersion.set("${project.version}")
        }
    }
}
