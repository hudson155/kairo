plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  api(project(":kairo-server:testing")) // Exposed for clients.
  api(project(":kairo-testing"))

  implementation(libs.guava) // For [Resources.getResource].
}
