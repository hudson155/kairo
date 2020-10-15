import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm")
  application
  id(Plugins.detekt)
  id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.monolith"
version = "0.1.0-SNAPSHOT"
application {
  mainClassName = project.properties.getOrDefault("mainClass", "io.ktor.server.cio.EngineMain") as String
}

dependencies {
  implementation(project(":limber-backend:common:server"))
  implementation(project(":limber-backend:common:sql"))

  implementation(project(":limber-backend:deprecated:common:module"))

  implementation(project(":limber-backend:module:auth:module"))
  implementation(project(":limber-backend:module:forms:module"))
  implementation(project(":limber-backend:module:health-check:module"))
  implementation(project(":limber-backend:module:orgs:module"))
  implementation(project(":limber-backend:module:users:module"))

  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Ktor.serverCio)
  implementation(Dependencies.Logging.logbackClassic)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("limber-monolith-server.jar")
  mergeServiceFiles()
  manifest {
    attributes(mapOf("MainClass" to application.mainClassName))
  }
}
