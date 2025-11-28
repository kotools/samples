rootProject.name = "samples"

private fun subproject(name: String, directoryName: String = name) {
    include(name)
    project(":$name").projectDir = rootDir.resolve("projects/$directoryName")
}

subproject("conventions")
subproject("demonstration")
subproject(name = "samples-gradle-plugin", directoryName = "plugin")

includeBuild("jvm")
includeBuild("jvm-demo")
