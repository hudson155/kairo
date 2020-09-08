import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
  id(Plugins.detekt)
  id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.backend"
version = "0.1.0-SNAPSHOT"
application {
  mainClassName = project.properties.getOrDefault("mainClass", "io.ktor.server.cio.EngineMain") as String
}

dependencies {
  implementation(project(":limber-backend-application:common:module")) // HealthCheckModule is defined in the application
  implementation(project(":limber-backend-application:common:sql"))
  implementation(project(":limber-backend-application:module:auth:auth-application"))
  implementation(project(":limber-backend-application:module:forms:forms-application"))
  implementation(project(":limber-backend-application:module:orgs:orgs-application"))
  implementation(project(":limber-backend-application:module:users:users-application"))
  implementation(project(":piper:application"))
  implementation(project(":piper:reps")) // HealthCheckModule rest interface is defined in the application
  implementation(project(":piper:sql"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Jackson.dataFormatYaml) // For config loader
  implementation(Dependencies.Logging.logbackClassic) // Used implicitly by Ktor
  implementation(Dependencies.Jackson.moduleKotlin) // For config loader
  implementation(Dependencies.Ktor.serverCio)
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
