package org.kotools.samples.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.diagnostics.TaskReportTask
import org.gradle.kotlin.dsl.withType

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
            .configureEach { this.displayGroup = MODULE_TASK_GROUP }
        project.pluginManager.withPlugin("base") {
            this@TasksPlugin.configureBaseLifecycleTasks(project)
        }
    }

    private fun configureBaseLifecycleTasks(project: Project): Unit =
        setOf("assemble", "check", "build")
            .map(project.tasks::named)
            .forEach {
                it.configure { this.group = MODULE_TASK_GROUP }
            }

    private companion object {
        private const val MODULE_TASK_GROUP: String = "module"
    }
}
