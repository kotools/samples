plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlinx.bcv)
}

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

kotlin {
    this.explicitApi()
    this.compilerOptions.allWarningsAsErrors.set(true)
}

gradlePlugin.plugins.register("Compatibility").configure {
    this.id = "convention." + this.name.lowercase()
    this.implementationClass =
        "org.kotools.samples.conventions.${this.name}Plugin"
}
gradlePlugin.plugins.register("Help").configure {
    this.id = "convention.help"
    this.implementationClass = "org.kotools.samples.conventions.HelpPlugin"
}

dependencies {
    this.implementation(libs.kotlin.gradle.plugin)

    this.testImplementation(this.gradleTestKit())
    this.testImplementation(libs.kotlin.test)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}

tasks.test.configure(Test::useJUnitPlatform)

private val moduleTaskGroup: String = "module"
tasks.tasks.configure { this.displayGroup = moduleTaskGroup }
tasks.check.configure { this.group = moduleTaskGroup }
