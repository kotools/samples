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

gradlePlugin.plugins.register("coordinates").configure {
    this.id = libs.plugins.conventions.coordinates.map { it.pluginId }
        .get()
    this.implementationClass = this.id.split('.')
        .dropLast(1)
        .plus("CoordinatesPlugin")
        .joinToString(separator = ".")
}
gradlePlugin.plugins.register("compatibility").configure {
    val packageRoot = "org.kotools.samples.conventions"
    this.id = "$packageRoot.compatibility"
    this.implementationClass = "$packageRoot.CompatibilityPlugin"
}

dependencies {
    this.testImplementation(this.gradleTestKit())
    this.testImplementation(libs.kotlin.test)
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
