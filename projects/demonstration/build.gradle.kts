plugins {
    this.alias(libs.plugins.kotlin.jvm)
    this.alias(libs.plugins.dokka)
    this.alias(libs.plugins.kotools.samples)
    this.alias(libs.plugins.convention.compatibility)
    this.alias(libs.plugins.convention.tasks)
}

repositories.mavenCentral()

compatibility {
    this.java.set(libs.versions.java)
    this.kotlin.set(libs.versions.kotlin)
}

kotlin.explicitApi()
