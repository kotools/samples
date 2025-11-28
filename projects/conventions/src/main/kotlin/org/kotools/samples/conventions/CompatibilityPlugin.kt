package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin that configures the compatibility of a project with its dependencies.
 */
public class CompatibilityPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project): Unit = Unit
}
