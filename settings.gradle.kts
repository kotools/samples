rootProject.name = "samples"

pluginManagement { this.includeBuild("projects/conventions") }

private fun subproject(name: String, directoryName: String = name) {
    include(name)
    project(":$name").projectDir = rootDir.resolve("projects/$directoryName")
}

subproject("demonstration")
subproject(name = "samples-gradle-plugin", directoryName = "plugin")

includeBuild("jvm")
includeBuild("jvm-demo")
