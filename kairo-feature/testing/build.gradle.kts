plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  api(project(":kairo-server:testing")) // Exposed for clients.
  api(project(":kairo-testing"))
}
