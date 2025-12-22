package conventions

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    val java: JavaPluginExtension = extensions.getByType()
    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = java.sourceCompatibility

    val kotlin: KotlinJvmProjectExtension = extensions.getByType()
    kotlin.explicitApi()
    kotlin.compilerOptions {
        this.allWarningsAsErrors.set(true)
        val version: KotlinVersion = KotlinVersion.KOTLIN_1_8
        this.apiVersion.set(version)
        this.languageVersion.set(version)
        this.jvmTarget.set(JvmTarget.JVM_17)
    }

    tasks.named<Test>("test").configure(Test::useJUnitPlatform)

    val moduleTaskGroup = "module"
    tasks.named<TaskReportTask>("tasks").configure {
        this.displayGroup = moduleTaskGroup
    }
    tasks.named("assemble").configure { this.group = moduleTaskGroup }
    tasks.named("check").configure { this.group = moduleTaskGroup }
    tasks.named("build").configure { this.group = moduleTaskGroup }
}
