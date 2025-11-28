plugins {
    this.`kotlin-dsl`
    this.alias(libs.plugins.kotlinx.bcv)
}

repositories.mavenCentral()

apiValidation.apiDumpDirectory = "src/api"

kotlin {
    this.explicitApi()
    this.compilerOptions.allWarningsAsErrors.set(true)
}

tasks {
    this.withType<ValidatePlugins>().configureEach {
        this.failOnWarning.set(true)
        this.enableStricterValidation.set(true)
    }

    this.test.configure(Test::useJUnitPlatform)

    val moduleTaskGroup = "module"
    this.tasks.configure { this.displayGroup = moduleTaskGroup }
    this.assemble.configure { this.group = moduleTaskGroup }
    this.check.configure { this.group = moduleTaskGroup }
    this.build.configure { this.group = moduleTaskGroup }
}
