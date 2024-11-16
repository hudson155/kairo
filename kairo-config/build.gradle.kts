plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-command-runner"))
  api(project(":kairo-environment-variable-supplier"))
  api(project(":kairo-gcp-secret-supplier"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string")) // Exposed for clients.
  api(project(":kairo-serialization")) // Exposed for clients.

  implementation(libs.guava) // For [Resources.getResource].

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
