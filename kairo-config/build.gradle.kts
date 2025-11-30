plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.hocon) // Available for usage.
  api(libs.serialization.hocon) // Available for usage.
  implementation(libs.serialization.json)

  testImplementation(project(":kairo-protected-string"))
  testImplementation(project(":kairo-testing"))
}
