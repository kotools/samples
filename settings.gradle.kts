rootProject.name = "samples"

include("samples")
project(":samples").projectDir = rootDir.resolve("projects/plugin")

includeBuild("jvm")
includeBuild("jvm-demo")
