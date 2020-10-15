plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:exception-mapping"))
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))

  api(Dependencies.Guice.guice)
  implementation(Dependencies.Ktor.clientCio)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}