import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:common"))
    api(Dependencies.Sql.exposed)
    api(Dependencies.Sql.flyway)
    api(Dependencies.Sql.hikari)
    api(Dependencies.Sql.postgres)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
