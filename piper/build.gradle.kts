plugins {
    kotlin("jvm")
    id(Plugins.detekt).version(Versions.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api(project(":piper:common"))
    api(project(":piper:data-conversion"))
    api(project(":piper:errors"))
    api(project(":piper:exception-mapping"))
    api(project(":piper:serialization"))
    implementation(Dependencies.Jackson.moduleKotlin)
    implementation(Dependencies.Ktor.serverHostCommon)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
