plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-integration-testing")) // Forced peer dependency.
  implementation(project(":kairo-sql:feature"))
  implementation(project(":kairo-sql:postgres"))
  implementation(project(":kairo-testing")) // Forced peer dependency.

  api(libs.exposed.jdbc)
  runtimeOnly(libs.postgres.jdbc)
  implementation(libs.testcontainers.postgres)
}
