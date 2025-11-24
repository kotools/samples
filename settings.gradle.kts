rootProject.name = "samples"

include("samples-gradle-plugin")
project(":samples-gradle-plugin").projectDir =
    rootDir.resolve("projects/gradle-plugin")

includeBuild("projects/conventions")
includeBuild("jvm")
includeBuild("jvm-demo")
