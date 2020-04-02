plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

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

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
