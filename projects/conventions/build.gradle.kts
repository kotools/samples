import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins { this.`kotlin-dsl` }

repositories.mavenCentral()

java {
    val version: JavaVersion = JavaVersion.VERSION_17
    this.sourceCompatibility = version
    this.targetCompatibility = version
}

kotlin {
    this.explicitApi()
    this.coreLibrariesVersion = libs.versions.kotlin.get()
    this.compilerOptions {
        this.allWarningsAsErrors.set(true)
        this.apiVersion.set(KotlinVersion.KOTLIN_2_0)
        this.languageVersion.set(KotlinVersion.KOTLIN_1_8)
        this.jvmTarget.set(JvmTarget.JVM_17)
    }
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
