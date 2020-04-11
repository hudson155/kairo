import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    application
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.backend"
version = "0.1.0-SNAPSHOT"
application {
    mainClassName = "io.limberapp.backend.ApplicationKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:module:auth:auth-application"))
    implementation(project(":limber-backend-application:module:forms:forms-application"))
    implementation(project(":limber-backend-application:module:orgs:orgs-application"))
    implementation(project(":limber-backend-application:module:users:users-application"))
    implementation(project(":piper"))
    implementation(project(":piper:sql"))
    api(Dependencies.Jwt.auth0JavaJwt)
    api(Dependencies.Jwt.auth0JwksRsa)
    implementation(Dependencies.Jackson.dataFormatYaml)
    implementation(Dependencies.Jackson.moduleKotlin)
    implementation(Dependencies.Ktor.serverCio)
    implementation(Dependencies.Logback.logbackClassic)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("limber-backend.jar")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("MainClass" to application.mainClassName))
    }
}
