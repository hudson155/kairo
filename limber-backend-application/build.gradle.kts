import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.backend"
version = "0.0.0"
application {
    mainClassName = "io.limberapp.backend.ApplicationKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:module:auth:application"))
    implementation(project(":limber-backend-application:module:forms:application"))
    implementation(project(":limber-backend-application:module:orgs:application"))
    implementation(project(":limber-backend-application:module:users:application"))
    implementation(project(":piper"))
    implementation(project(":piper:sql"))
    api(Dependencies.Jwt.auth0JavaJwt)
    api(Dependencies.Jwt.auth0JwksRsa)
    implementation(Dependencies.Jackson.dataFormatYaml)
    implementation(Dependencies.Ktor.serverCio)
    implementation(Dependencies.Logback.logbackClassic)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("limber-backend.jar")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("MainClass" to application.mainClassName))
    }
}
