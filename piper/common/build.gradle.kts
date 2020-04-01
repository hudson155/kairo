dependencies {
    api(project(":piper:data-conversion"))
    api(project(":piper:exceptions"))
    api(project(":piper:ktor-auth"))
    api(project(":piper:object-mapper"))
    api(project(":piper:reps"))
    api(project(":piper:util"))
    api(Dependencies.Guice.guice)
    api(Dependencies.Jackson.moduleKotlin)
    api(Dependencies.Ktor.serverCore)
}
