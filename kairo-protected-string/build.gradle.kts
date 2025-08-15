plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(libs.serialization.core)

  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.json)
}
