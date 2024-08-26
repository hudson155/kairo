plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string")) // Exposed for clients.
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  implementation(libs.guava) // For [Resources.getResource].

  testImplementation(project(":kairo-logging"))
  testImplementation(project(":testing"))
}
