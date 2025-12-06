plugins {
    this.alias(libs.plugins.kotlin.jvm)
    this.alias(libs.plugins.kotools.samples)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

repositories.mavenCentral()

kotlin.coreLibrariesVersion = libs.versions.kotlin.get()
