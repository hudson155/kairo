plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  api(project(":kairo-protected-string"))
  implementation(project(":kairo-serialization"))
  api(project(":kairo-sql"))

  implementation(libs.r2dbc.pool)
}
