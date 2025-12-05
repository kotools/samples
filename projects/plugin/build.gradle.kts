plugins {
    this.`kotlin-dsl`
    this.alias(libs.plugins.kotlinx.bcv)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

kotlinJvmConventions.coreLibrariesVersion.set(libs.versions.kotlin)

gradlePlugin {
    this.vcsUrl.set("https://github.com/kotools/samples")
    this.plugins.register("org.kotools.samples").configure {
        this.displayName = "Kotools Samples"
        this.description =
            "Plugin that inlines read-only Kotlin samples into documentation."
        this.id = this.name
        this.implementationClass = "${this.name}.KotoolsSamplesPlugin"
    }
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}
