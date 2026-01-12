plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-optional")) // Available for usage.
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.ktorServer) // Available for usage.

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-testing"))

  implementation(project(":kairo-id")) // TODO: REMOVE
}
