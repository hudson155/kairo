plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-serialization"))

  api(libs.moneta)

  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.json)
}
