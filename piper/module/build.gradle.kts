plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:config")) // Provides config to modules
    api(project(":piper:exceptions")) // Provides exceptions to modules
    api(project(":piper:ktor-auth")) // All modules use auth
    implementation(project(":piper:reps"))
    api(project(":piper:rest-interface")) // Allows modules to configure endpoints
    api(project(":piper:serialization")) // All modules configure serialization
    api(project(":piper:util"))
    api(Dependencies.Guice.guice) // Modules are Guice modules
    api(Dependencies.Ktor.serverCore) // Every modules needs this
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
