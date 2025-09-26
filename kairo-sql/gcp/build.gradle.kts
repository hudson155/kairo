plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-sql:feature")) // Forced peer dependency.

  runtimeOnly(libs.postgres.gcp)
}
