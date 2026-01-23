plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-integration-testing"))
  compileOnly(project(":kairo-sql:feature"))
  compileOnly(project(":kairo-sql:postgres"))
  compileOnly(project(":kairo-testing"))

  /**
   * Although R2DBC is typically used in Kairo applications,
   * JDBC is used here because R2DBC does not have DDL support.
   */
  api(libs.exposed.jdbc)
  runtimeOnly(libs.postgres.jdbc)
  implementation(libs.testcontainers.postgres)
}
