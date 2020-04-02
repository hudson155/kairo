import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt).version(Versions.detekt)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:common"))
    api(project(":piper:errors"))
    api(project(":piper:exception-mapping"))
    implementation(Dependencies.Ktor.jackson)
    implementation(Dependencies.Ktor.serverHostCommon)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
