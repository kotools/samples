package org.kotools.samples.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.tasks.ValidatePlugins
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/** Project plugin that configures the `kotlin-dsl` Gradle Core plugin. */
public class KotlinDslPlugin internal constructor() : Plugin<Project> {
    /** Applies this plugin to the specified [project]. */
    override fun apply(project: Project) {
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val kotlin: KotlinJvmProjectExtension =
                project.extensions.getByType()
            kotlin.explicitApi()
            kotlin.compilerOptions.allWarningsAsErrors.set(true)

            project.tasks.named<Test>("test")
                .configure(Test::useJUnitPlatform)
        }
        project.tasks.withType<ValidatePlugins>().configureEach {
            this.failOnWarning.set(true)
            this.enableStricterValidation.set(true)
        }
    }
}
