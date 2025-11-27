plugins {
    this.`kotlin-dsl`
    this.alias(libs.plugins.kotlinx.bcv)
    this.alias(libs.plugins.convention.compatibility)
    this.alias(libs.plugins.convention.help)
    this.alias(libs.plugins.convention.kotlin.dsl)
    this.alias(libs.plugins.convention.tasks)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

compatibility {
    this.java.set(libs.versions.java)
    this.kotlin.set(libs.versions.kotlin)
}

gradlePlugin {
    this.vcsUrl.set("https://github.com/kotools/samples")
    this.plugins.register("org.kotools.samples").configure {
        this.displayName = "Kotools Samples"
        this.description =
            "Plugin that inlines read-only Kotlin samples into documentation."
        this.id = this.name
        this.implementationClass = "${this.name}.gradle.KotoolsSamplesPlugin"
    }
}

dependencies {
    this.implementation(libs.kotlin.gradle.plugin)

    this.testImplementation(libs.kotlin.test)
}

tasks.test.configure(Test::useJUnitPlatform)
