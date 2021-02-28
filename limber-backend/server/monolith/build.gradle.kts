import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  application
  id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.server"
version = "0-SNAPSHOT"
application {
  mainClassName ="io.ktor.server.cio.EngineMain" // Shadow requires use of deprecated mainClassName.
}

dependencies {
  implementation(project(":limber-backend:common:server"))
  implementation(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:db:limber"))
  implementation(project(":limber-backend:module:health-check:module"))
  implementation(project(":limber-backend:module:orgs:module"))
  implementation(project(":limber-backend:module:users:module"))
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("limber-monolith-server.jar")
  mergeServiceFiles()
  manifest {
    attributes(mapOf("MainClass" to application.mainClassName))
  }
}
