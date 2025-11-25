package org.kotools.samples.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.diagnostics.TaskReportTask
import org.gradle.kotlin.dsl.withType
import org.kotools.samples.gradle.conventions.internal.TaskGroup

/**
 * Project plugin that configures the group displayed by the `tasks` task, and
 * the group of lifecycle tasks provided by
 * [Base Plugin](https://docs.gradle.org/8.12.1/userguide/base_plugin.html)
 * (`assemble`, `check` and `build`).
 */
public class TasksPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        project.tasks.withType<TaskReportTask>()
            .configureEach { TaskGroup.Module.displayIn(this) }
        project.pluginManager.withPlugin("base") {
            this@TasksPlugin.configureBaseLifecycleTasks(project)
        }
    }

    private fun configureBaseLifecycleTasks(project: Project): Unit =
        setOf("assemble", "check", "build")
            .map(project.tasks::named)
            .forEach {
                it.configure { TaskGroup.Module.group(this) }
            }
}
