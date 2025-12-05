plugins {
    this.alias(libs.plugins.kotlin.jvm)
    this.alias(libs.plugins.kotools.samples)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

repositories.mavenCentral()

kotlinJvmConventions.coreLibrariesVersion.set(libs.versions.kotlin)
