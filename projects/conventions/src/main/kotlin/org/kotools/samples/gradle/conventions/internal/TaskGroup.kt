package org.kotools.samples.gradle.conventions.internal

import org.gradle.api.Task
import org.gradle.api.tasks.diagnostics.TaskReportTask

internal enum class TaskGroup {
    Module;

    fun displayIn(task: TaskReportTask) {
        task.displayGroup = this.toString()
    }

    fun group(task: Task) {
        task.group = this.toString()
    }

    override fun toString(): String = this.name.lowercase()
}
