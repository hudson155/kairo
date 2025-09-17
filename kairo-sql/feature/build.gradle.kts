plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  api(project(":kairo-protected-string")) // In config.
  implementation(project(":kairo-serialization"))
  api(project(":kairo-sql"))

  api(libs.hikari)
}
