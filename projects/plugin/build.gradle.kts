plugins {
    this.`kotlin-dsl`
    this.signing
    this.alias(libs.plugins.kotlinx.bcv)
    this.alias(libs.plugins.gradle.plugin.publish)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

group = "org.kotools"
version = "0.6.0-SNAPSHOT"

repositories {
    this.mavenCentral()
    this.maven("https://jitpack.io")
}

apiValidation.apiDumpDirectory = "src/api"

kotlin.coreLibrariesVersion = libs.versions.kotlin.get()

gradlePlugin {
    this.vcsUrl.set("https://github.com/kotools/samples")
    this.website.set(this.vcsUrl)
    this.plugins.register("samples").configure {
        this.displayName = "Kotools Samples"
        this.description =
            "Plugin that inlines read-only Kotlin samples into documentation."
        this.id = "org.kotools.${this.name}"
        this.implementationClass = "${this.id}.KotoolsSamplesPlugin"
        this.tags = setOf("kotlin", "dokka", "documentation")
    }
}

signing {
    val gpgPrivateKey: String? = System.getenv("GPG_PRIVATE_KEY")
    val gpgPassword: String? = System.getenv("GPG_PASSWORD")
    this.useInMemoryPgpKeys(gpgPrivateKey, gpgPassword)
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
