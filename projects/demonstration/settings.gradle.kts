rootProject.name = "demonstration"

pluginManagement.includeBuild("../plugin")

dependencyResolutionManagement.versionCatalogs.register("libs").configure {
    val catalog: ConfigurableFileCollection =
        files("../../gradle/libs.versions.toml")
    this.from(catalog)
}
