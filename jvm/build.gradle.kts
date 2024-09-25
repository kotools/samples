plugins {
    `kotlin-dsl`
    alias(libs.plugins.gradle.plugin.publish)
}

group = "org.kotools"
version = "0.1.0-SNAPSHOT"

repositories.mavenCentral()

kotlin {
    explicitApi()
    jvmToolchain(17)
}

gradlePlugin.plugins.named("org.kotools.samples.jvm").configure {
    displayName = "Kotools Samples"
    description = "Gradle plugin that inlines read-only Kotlin and Java code " +
            "samples into Dokka documentation, ensuring they are always " +
            "correct and visible not only online but also in IDEs."
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)
    testImplementation(libs.kotlin.test.junit5)
}

tasks.withType<ValidatePlugins>().configureEach {
    failOnWarning.set(true)
    enableStricterValidation.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)
