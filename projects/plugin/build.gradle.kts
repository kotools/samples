plugins {
    this.`kotlin-dsl`
    this.`maven-publish`
    this.signing
    this.alias(libs.plugins.kotlinx.bcv)
    this.alias(libs.plugins.conventions.kotlin.jvm)
}

group = "org.kotools"
version = "0.5.0-SNAPSHOT"

private val organizationName: String = "Kotools"
private val organizationGitHub: String = "https://github.com/kotools"

private val productName: String = "$organizationName Samples"
private val productDescription: String =
    "Plugin that inlines read-only Kotlin samples into documentation."
private val productGitHub: String = "$organizationGitHub/samples"

repositories {
    this.mavenCentral()
    this.maven("https://jitpack.io")
}

apiValidation.apiDumpDirectory = "src/api"

kotlin.coreLibrariesVersion = libs.versions.kotlin.get()

gradlePlugin {
    this.vcsUrl.set(productGitHub)
    this.plugins.register("samples").configure {
        this.displayName = productName
        this.description = productDescription
        this.id = "org.kotools.${this.name}"
        this.implementationClass = "${this.id}.KotoolsSamplesPlugin"
    }
}

publishing {
    this.repositories.maven {
        this.name = "project"
        this.url = uri(layout.buildDirectory.dir("maven"))
    }
    this.publications.withType<MavenPublication>().configureEach {
        this.pom {
            this.name = productName
            this.description = productDescription
            this.url = productGitHub
            this.licenses {
                this.license {
                    this.name = "MIT"
                    this.url = "https://opensource.org/licenses/MIT"
                }
            }
            this.developers {
                this.developer {
                    this.name = "Loïc Lamarque"
                    this.email = "contact@kotools.org"
                    this.organization = organizationName
                    this.organizationUrl = organizationGitHub
                }
            }
            this.scm {
                this.url = this@pom.url
                this.connection = this.url.map { "${it}.git" }
            }
        }
    }
}

signing {
    val gpgPrivateKey: String? = System.getenv("GPG_PRIVATE_KEY")
    val gpgPassword: String? = System.getenv("GPG_PASSWORD")
    if (gpgPrivateKey != null && gpgPassword != null) {
        this.useInMemoryPgpKeys(gpgPrivateKey, gpgPassword)
        this.sign(publishing.publications)
    }
}

dependencies {
    this.implementation(libs.dokka.gradle.plugin)
    this.implementation(libs.kotlin.gradle.plugin)
    this.implementation(libs.kotlinx.ast)

    this.testImplementation(this.gradleTestKit())
    this.testImplementation(libs.kotlin.test)
}

tasks.withType<ValidatePlugins>().configureEach {
    this.failOnWarning.set(true)
    this.enableStricterValidation.set(true)
}

tasks.withType<PublishToMavenRepository>().configureEach {
    val signTasks: TaskCollection<Sign> = tasks.withType<Sign>()
    this.dependsOn(signTasks)
}

private val zipSourcePublication: TaskProvider<Zip> by tasks.registering(
    Zip::class
) {
    this.dependsOn(
        tasks.named("publishPluginMavenPublicationToProjectRepository")
    )
    this.archiveBaseName = "kotools-samples"
    this.archiveVersion = project.version.toString()
    this.from(rootProject.layout.buildDirectory.dir("maven/"))
}

private val publishSources: TaskProvider<Task> by tasks.registering
publishSources.configure {
    this.description = "Publishes the sources of this project."
    this.group = "publishing"
    this.dependsOn(zipSourcePublication)
}
