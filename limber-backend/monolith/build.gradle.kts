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
  implementation(project(":limber-backend:common:application"))
  implementation(project(":limber-backend:common:health-check-module"))
  implementation(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:monolith:common:module"))
  implementation(project(":limber-backend:monolith:common:sql"))
  implementation(project(":limber-backend:monolith:module:auth:auth-application"))
  implementation(project(":limber-backend:monolith:module:forms:forms-application"))
  implementation(project(":limber-backend:monolith:module:orgs:orgs-application"))
  implementation(project(":limber-backend:monolith:module:users:users-application"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Jackson.dataFormatYaml)
  implementation(Dependencies.Logging.logbackClassic)
  implementation(Dependencies.Jackson.moduleKotlin)
  implementation(Dependencies.Ktor.serverCio)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("limber-backend-monolith.jar")
  mergeServiceFiles()
  manifest {
    attributes(mapOf("MainClass" to application.mainClassName))
  }
}
