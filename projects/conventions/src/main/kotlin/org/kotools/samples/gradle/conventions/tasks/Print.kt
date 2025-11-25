package org.kotools.samples.gradle.conventions.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/** Task that prints the specified [message] to the standard output. */
@DisableCachingByDefault(because = "Prints to standard output.")
public abstract class Print internal constructor() : DefaultTask() {
    /** The message to print. */
    @get:Input
    public abstract val message: Property<String>

    @TaskAction
    internal fun execute(): Unit = println(this.message.get())
}
