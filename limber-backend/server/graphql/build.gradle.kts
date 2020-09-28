import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm")
  application
  id(Plugins.detekt)
  id(Plugins.shadow).version(Versions.shadow)
}

group = "io.limberapp.graphqlServer"
version = "0.1.0-SNAPSHOT"
application {
  mainClassName = project.properties.getOrDefault("mainClass", "io.ktor.server.cio.EngineMain") as String
}

dependencies {
  implementation(project(":limber-backend:common:server"))

  implementation(project(":limber-backend:module:graphql:module"))
  implementation(project(":limber-backend:module:health-check:module"))

  implementation(Dependencies.Ktor.serverCio)
  implementation(Dependencies.Logging.logbackClassic)

  testImplementation(project(":limber-backend:common:testing"))
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("limber-graphql-server.jar")
  mergeServiceFiles()
  manifest {
    attributes(mapOf("MainClass" to application.mainClassName))
  }
}
