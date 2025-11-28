rootProject.name = "demonstration"

pluginManagement {
    this.includeBuild("../conventions")
    this.includeBuild("../plugin")
}

dependencyResolutionManagement.versionCatalogs.register("libs").configure {
    val catalog: ConfigurableFileCollection =
        files("../../gradle/libs.versions.toml")
    this.from(catalog)
}
