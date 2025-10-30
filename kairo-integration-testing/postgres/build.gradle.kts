plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-integration-testing")) // Forced peer dependency.
  implementation(project(":kairo-sql:feature"))
  implementation(project(":kairo-sql:postgres"))
  implementation(project(":kairo-testing")) // Forced peer dependency.

  implementation(libs.exposed.jdbc)
  implementation(libs.testcontainers.postgres)
}
