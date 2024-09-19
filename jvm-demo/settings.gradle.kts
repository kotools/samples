rootProject.name = "samples-jvm-demo"

pluginManagement.includeBuild("../jvm")

dependencyResolutionManagement.versionCatalogs.register("libs") {
    val file: ConfigurableFileCollection = files("../gradle/libs.versions.toml")
    from(file)
}

gradle.startParameter.isDryRun = gradle.parent?.startParameter?.isDryRun == true
