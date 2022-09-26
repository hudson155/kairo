plugins {
  id("limber-jvm")
}

dependencies {
  implementation(project(":common:serialization"))
  api(project(":feature:rest:interface"))

  api(Dependencies.Google.guice) // Make Guice available to library users.
  implementation(Dependencies.Ktor.Client.cio)
  implementation(Dependencies.Ktor.Client.contentNegotiation)
  api(Dependencies.Ktor.Client.coreJvm)
  implementation(Dependencies.Ktor.Serialization.jackson)
}
