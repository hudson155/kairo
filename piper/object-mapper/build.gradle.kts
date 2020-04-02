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
    implementation(project(":piper:data-conversion"))
    implementation(Dependencies.Jackson.dataTypeJsr310)
    api(Dependencies.Jackson.moduleKotlin)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
