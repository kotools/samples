rootProject.name = "samples-gradle-plugin"

dependencyResolutionManagement.versionCatalogs.register("libs").configure {
    val catalog: ConfigurableFileCollection =
        files("../../gradle/libs.versions.toml")
    this.from(catalog)
}
