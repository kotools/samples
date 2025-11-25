plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinx.bcv)
    alias(libs.plugins.convention.compatibility)
    alias(libs.plugins.convention.help)
    alias(libs.plugins.convention.kotlin.dsl)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

compatibility {
    this.java.set(libs.versions.java)
    this.kotlin.set(libs.versions.kotlin)
}

private val moduleTaskGroup: String = "module"
tasks.tasks.configure { this.displayGroup = moduleTaskGroup }
tasks.assemble.configure { this.group = moduleTaskGroup }
tasks.check.configure { this.group = moduleTaskGroup }
tasks.build.configure { this.group = moduleTaskGroup }
