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
