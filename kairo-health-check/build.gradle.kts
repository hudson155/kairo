plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest"))

  implementation(libs.serialization.core)
}
