plugins { `kotlin-dsl` }

group = "org.kotools"
version = "0.1.0-SNAPSHOT"

repositories.mavenCentral()

kotlin {
    explicitApi()
    jvmToolchain(17)
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
