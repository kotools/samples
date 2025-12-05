import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins { this.`kotlin-dsl` }

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

repositories.mavenCentral()

// ------------------------------- Compatibility -------------------------------

java {
    this.sourceCompatibility = JavaVersion.VERSION_17
    this.targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    val version: Provider<KotlinVersion> = libs.versions.kotlin
        .map { it.substringBeforeLast(delimiter = '.') }
        .map(KotlinVersion.Companion::fromVersion)
    this.compilerOptions.apiVersion.set(version)
    this.compilerOptions.languageVersion.set(version)
    this.coreLibrariesVersion = libs.versions.kotlin.get()

    this.compilerOptions.jvmTarget.set(
        JvmTarget.fromTarget("${java.targetCompatibility}")
    )
    this.compilerOptions.freeCompilerArgs.add(
        "-Xjdk-release=${java.sourceCompatibility}"
    )
}

tasks.register("compatibilities").configure {
    this.description = "Prints detected compatibilities."
    this.group = "help"

    this.doLast {
        val javaSourceVersion: JavaVersion = java.sourceCompatibility
        val javaTargetVersion: JavaVersion = java.targetCompatibility
        if (javaSourceVersion == javaTargetVersion)
            println("Java $javaSourceVersion")
        else println("Java $javaSourceVersion (target: $javaTargetVersion)")

        val kotlinVersion: String = kotlin.coreLibrariesVersion
        val kotlinApiVersion: String = kotlin.compilerOptions.apiVersion.get()
            .version
        val kotlinLanguageVersion: String = kotlin.compilerOptions
            .languageVersion
            .get()
            .version
        println(
            "Kotlin $kotlinVersion (api: $kotlinApiVersion, " +
                    "language: $kotlinLanguageVersion)"
        )

        println("Gradle ${gradle.gradleVersion}")

        val dokkaVersion: String = libs.versions.dokka.get()
        println("Dokka $dokkaVersion")
    }
}

// -------------------------------- Kotlin DSL ---------------------------------

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
}

// ----------------------------------- Tasks -----------------------------------

tasks {
    val moduleTaskGroup = "module"
    this.tasks.configure { this.displayGroup = moduleTaskGroup }
    this.assemble.configure { this.group = moduleTaskGroup }
    this.check.configure { this.group = moduleTaskGroup }
    this.build.configure { this.group = moduleTaskGroup }
}
