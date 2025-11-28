rootProject.name = "samples"

private fun subproject(name: String, directoryName: String = name) {
    include(name)
    project(":$name").projectDir = rootDir.resolve("projects/$directoryName")
}

subproject("demonstration")

includeBuild("projects/conventions")
includeBuild("projects/plugin")
includeBuild("jvm")
includeBuild("jvm-demo")
