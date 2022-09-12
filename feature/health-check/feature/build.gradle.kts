plugins {
  id("limber-jvm")
}

dependencies {
  // The health check REST feature uses the HTTP client implementation by default.
  // Most REST features use the Local HTTP client implementation by default.
  // This is why the client module is exposed in the API.
  api(project(":feature:health-check:client"))

  api(project(":common:feature"))

  implementation(project(":feature:rest:feature"))
  testImplementation(project(":feature:rest:testing"))
}
