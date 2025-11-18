import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins { `kotlin-dsl` }

repositories.mavenCentral()

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

tasks.withType<JavaCompile>().configureEach {
    val release: Provider<Int> = libs.versions.java.map(String::toInt)
    this.options.release.set(release)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)
