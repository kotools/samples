plugins {
    this.alias(libs.plugins.kotlin.jvm)
    this.alias(libs.plugins.kotools.samples)
    this.alias(libs.plugins.conventions.kotlin.jvm)
    this.alias(libs.plugins.dokka)
}

repositories.mavenCentral()

kotlin.coreLibrariesVersion = libs.versions.kotlin.get()

dependencies.testImplementation(libs.kotlin.test)
