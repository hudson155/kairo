plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-config")) // Exposed for clients.
  api(project(":kairo-dependency-injection")) // Exposed for clients.
  implementation(project(":kairo-logging"))
  api(project(":kairo-util")) // Exposed for clients.
}
