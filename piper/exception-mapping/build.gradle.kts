import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:errors"))
    api(project(":piper:exceptions"))
    api(project(":piper:util"))
    implementation(Dependencies.Ktor.httpJvm)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
