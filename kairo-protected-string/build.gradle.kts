plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(libs.serialization)

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.json)
}
