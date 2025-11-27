rootProject.name = "demonstration"

pluginManagement.includeBuild("../plugin")

dependencyResolutionManagement.versionCatalogs.register("libs") {
    gradle.parent?.rootProject?.layout?.projectDirectory
        ?.files("gradle/libs.versions.toml")
        ?.let(this::from)
}

gradle.startParameter.isDryRun = gradle.parent?.startParameter?.isDryRun == true
