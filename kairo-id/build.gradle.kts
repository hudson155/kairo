plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.core)
  testImplementation(libs.serialization.json)
}
