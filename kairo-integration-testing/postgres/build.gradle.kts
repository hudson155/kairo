plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-integration-testing")) // Forced peer dependency.
  compileOnly(project(":kairo-sql:feature")) // Forced peer dependency.
  compileOnly(project(":kairo-sql:postgres")) // Forced peer dependency.
  compileOnly(project(":kairo-testing")) // Forced peer dependency.

  /**
   * Although R2DBC is typically used in Kairo applications,
   * JDBC is used here because R2DBC does not have DDL support.
   */
  api(libs.exposed.jdbc)
  runtimeOnly(libs.postgres.jdbc)
  implementation(libs.testcontainers.postgres)
}
