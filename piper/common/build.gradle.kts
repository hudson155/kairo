import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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
