import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinx.bcv)
}

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

kotlin {
    this.explicitApi()
    this.compilerOptions {
        this.allWarningsAsErrors.set(true)

        val kotlinVersion: Provider<KotlinVersion> = libs.versions.kotlin
            .map { it.substringBeforeLast('.') }
            .map(KotlinVersion.Companion::fromVersion)
        this.apiVersion.set(kotlinVersion)
        this.languageVersion.set(kotlinVersion)

        val javaVersion: Provider<String> = libs.versions.java
        val jvmTarget: Provider<JvmTarget> =
            javaVersion.map(JvmTarget.Companion::fromTarget)
        this.jvmTarget.set(jvmTarget)
        val release: Provider<String> = javaVersion.map { "-Xjdk-release=$it" }
        this.freeCompilerArgs.add(release)
    }
    this.coreLibrariesVersion = libs.versions.kotlin.get()
}

gradlePlugin.plugins.register("Help").configure {
    this.id = "convention.help"
    this.implementationClass = "org.kotools.samples.conventions.HelpPlugin"
}

dependencies.testImplementation(libs.kotlin.test)

tasks.withType<JavaCompile>().configureEach {
    val release: Provider<Int> = libs.versions.java.map(String::toInt)
    this.options.release.set(release)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)
