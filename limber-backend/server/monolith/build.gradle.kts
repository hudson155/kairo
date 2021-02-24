import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  application
  id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.server"
version = "0-SNAPSHOT"
application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
  implementation(project(":limber-backend:common:server"))
  implementation(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:module:health-check:module"))
  implementation(project(":limber-backend:module:users:module"))
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("limber-monolith-server.jar")
  mergeServiceFiles()
  manifest {
    attributes(mapOf("MainClass" to application.mainClassName))
  }
}
