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

gradlePlugin {
    fun registerPlugin(
        name: String,
        id: String = "convention." + name.lowercase()
    ) {
        this.plugins.register(name).configure {
            this.id = id
            this.implementationClass =
                "org.kotools.samples.gradle.conventions.${this.name}Plugin"
        }
    }

    registerPlugin("Compatibility")
    registerPlugin("Help")
    registerPlugin(name = "KotlinDsl", id = "convention.kotlin.dsl")
    registerPlugin("Tasks")
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
tasks.assemble.configure { this.group = moduleTaskGroup }
tasks.check.configure { this.group = moduleTaskGroup }
tasks.build.configure { this.group = moduleTaskGroup }
