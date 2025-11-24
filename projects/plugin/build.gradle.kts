plugins {
    `kotlin-dsl`
    alias(libs.plugins.convention.compatibility)
    alias(libs.plugins.convention.help)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories.mavenCentral()

compatibility {
    this.java.set(libs.versions.java)
    this.kotlin.set(libs.versions.kotlin)
}

kotlin {
    this.explicitApi()
    this.compilerOptions.allWarningsAsErrors.set(true)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)
