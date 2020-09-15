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
  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Jackson.dataFormatYaml) // For config loader
  implementation(Dependencies.Logging.logbackClassic) // Used implicitly by Ktor
  implementation(Dependencies.Jackson.moduleKotlin) // For config loader
  implementation(Dependencies.Ktor.serverCio)
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
