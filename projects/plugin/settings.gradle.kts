rootProject.name = "samples-gradle-plugin"

pluginManagement.includeBuild("../conventions")

dependencyResolutionManagement.versionCatalogs.register("libs").configure {
    val catalog: ConfigurableFileCollection =
        files("../../gradle/libs.versions.toml")
    this.from(catalog)
}
