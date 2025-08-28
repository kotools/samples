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

    testImplementation(libs.kotlin.test)
}

tasks.test.configure(Test::useJUnitPlatform)
