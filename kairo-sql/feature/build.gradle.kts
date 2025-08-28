plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  api(project(":kairo-protected-string"))
  api(project(":kairo-sql"))

  implementation(libs.r2dbc.pool)
  implementation(libs.serialization.core)
}
