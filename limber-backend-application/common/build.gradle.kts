import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":limber-backend-application:common:api"))
    api(project(":piper:common"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
