import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.gradle.plugin.publish)
}

group = "org.kotools"
version = "0.4.0-SNAPSHOT"

repositories.mavenCentral()

kotlin {
    explicitApi()
    jvmToolchain(17)
    compilerOptions {
        allWarningsAsErrors.set(true)
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    vcsUrl = "https://github.com/kotools/samples"
    website = vcsUrl
    plugins.named("org.kotools.samples.jvm").configure {
        displayName = "Kotools Samples"
        description = "Gradle plugin that inlines read-only Kotlin and Java " +
                "code samples into Dokka documentation, ensuring they are " +
                "always correct and visible not only online but also in IDEs."
        tags = setOf("kotlin", "java", "dokka")
    }
}

signing {
    val privateKey: String? = System.getenv("GPG_PRIVATE_KEY")
    val password: String? = System.getenv("GPG_PASSWORD")
    useInMemoryPgpKeys(privateKey, password)
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.dokka.gradle.plugin)

    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    withType<ValidatePlugins>().configureEach {
        failOnWarning.set(true)
        enableStricterValidation.set(true)
    }
    withType<AbstractTestTask>().configureEach {
        testLogging {
            events = setOf(TestLogEvent.FAILED)
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = false
        }
    }
    test.configure(Test::useJUnitPlatform)
}
