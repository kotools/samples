plugins {
    this.`kotlin-dsl`
    this.alias(libs.plugins.kotlinx.bcv)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories {
    this.mavenCentral()
    this.maven("https://jitpack.io")
}

apiValidation.apiDumpDirectory = "src/api"

kotlin.coreLibrariesVersion = libs.versions.kotlin.get()

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

dependencies {
    this.implementation(libs.dokka.gradle.plugin)
    this.implementation(libs.kotlin.gradle.plugin)
    this.implementation(libs.kotlinx.ast)

    this.testImplementation(this.gradleTestKit())
    this.testImplementation(libs.kotlin.test)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}
