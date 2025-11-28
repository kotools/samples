group = "org.kotools"
version = "0.5.0-SNAPSHOT"

tasks.register("coordinates").configure {
    this.description = "Prints coordinates (group:artifact:version)."
    this.group = "help"

    this.doLast {
        println("${project.group}:${project.name}:${project.version}")
    }
}
