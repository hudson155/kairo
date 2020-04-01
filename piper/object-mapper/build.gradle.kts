dependencies {
    implementation(project(":piper:data-conversion"))
    implementation(Dependencies.Jackson.dataTypeJsr310)
    api(Dependencies.Jackson.moduleKotlin)
}
