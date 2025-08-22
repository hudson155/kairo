plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  api(project(":kairo-id"))

  api(libs.koin.core)
  implementation(libs.serialization.core)
}
