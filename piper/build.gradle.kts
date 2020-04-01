dependencies {
    api(project(":piper:common"))
    api(project(":piper:errors"))
    api(project(":piper:exception-mapping"))
    implementation(Dependencies.Ktor.jackson)
    implementation(Dependencies.Ktor.serverHostCommon)
}

subprojects {
    if (project.path != ":piper:util") {
        dependencies {
            implementation(project(":piper:util"))
        }
    }
}
