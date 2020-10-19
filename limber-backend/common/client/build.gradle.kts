plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:exception-mapping"))
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))

  api(Dependencies.Google.guice) // Not used by this module - provided to consumers.
  implementation(Dependencies.Ktor.clientCio)
  implementation(Dependencies.Logging.slf4j)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
