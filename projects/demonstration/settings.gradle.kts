rootProject.name = "demonstration"

pluginManagement {
    this.repositories {
        this.gradlePluginPortal()
        this.maven("https://jitpack.io")
    }
    this.includeBuild("../conventions")
    this.includeBuild("../plugin")
}

dependencyResolutionManagement.versionCatalogs.register("libs").configure {
    val catalog: ConfigurableFileCollection =
        files("../../gradle/libs.versions.toml")
    this.from(catalog)
}
