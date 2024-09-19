plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotools.samples.jvm)
}

repositories.mavenCentral()

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    sampleImplementation(libs.kotlin.test.junit5)
}

tasks.test.configure(Test::useJUnitPlatform)
