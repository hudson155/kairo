import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:feature"))
    api(project(":common:serialization"))
    api(project(":feature:rest:interface"))

    // Ktor core.
    implementation(Dependencies.Ktor.Server.cio)
    api(Dependencies.Ktor.Server.coreJvm)
    implementation(Dependencies.Ktor.Server.hostCommonJvm)

    // Ktor plugins.
    implementation(Dependencies.Ktor.Serialization.jackson)
    implementation(Dependencies.Ktor.Server.authJvm)
    implementation(Dependencies.Ktor.Server.autoHeadResponse)
    implementation(Dependencies.Ktor.Server.compression)
    implementation(Dependencies.Ktor.Server.contentNegotiation)
    implementation(Dependencies.Ktor.Server.cors)
    implementation(Dependencies.Ktor.Server.dataConversion)
    implementation(Dependencies.Ktor.Server.defaultHeaders)
    implementation(Dependencies.Ktor.Server.doubleReceive)
    implementation(Dependencies.Ktor.Server.forwardedHeaders)
    implementation(Dependencies.Ktor.Server.statusPages)

    // Auth
    api(Dependencies.Auth.auth0JavaJwt)
    implementation(Dependencies.Auth.auth0JwksRsa)
    api(Dependencies.Ktor.Server.authJwt)

    // MockK is used in production code for endpoint template generation.
    implementation(Dependencies.Testing.mockK)
  }
}
