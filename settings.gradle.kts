rootProject.name = "samples"

include("samples")
project(":samples").projectDir = rootDir.resolve("projects/plugin")

includeBuild("projects/conventions")
includeBuild("jvm")
includeBuild("jvm-demo")
