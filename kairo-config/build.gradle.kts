plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-environment-variable-supplier"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string")) // Exposed for clients.
  implementation(project(":kairo-serialization"))

  implementation(libs.guava) // For [Resources.getResource].

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
