plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-money"))
  implementation(project(":kairo-serialization")) // Peer dependency.

  testImplementation(project(":kairo-testing"))
}
