plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api(project(":piper:exceptions"))
    api(project(":piper:ktor-auth"))
    api(project(":piper:reps"))
    implementation(project(":piper:serialization"))
    api(project(":piper:util"))
    api(Dependencies.Guice.guice)
    api(Dependencies.Jackson.moduleKotlin)
    api(Dependencies.Ktor.serverCore)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
