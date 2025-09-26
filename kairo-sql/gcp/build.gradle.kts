plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-sql:feature"))

  runtimeOnly(libs.postgres.gcp)
}
