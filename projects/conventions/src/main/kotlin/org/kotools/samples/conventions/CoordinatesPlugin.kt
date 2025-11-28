package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

/** Plugin that configure the coordinates of a project. */
public class CoordinatesPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {}
}
