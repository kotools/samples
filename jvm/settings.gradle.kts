rootProject.name = "samples-jvm"

dependencyResolutionManagement.versionCatalogs.register("libs") {
    val file: ConfigurableFileCollection = files("../gradle/libs.versions.toml")
    from(file)
}
