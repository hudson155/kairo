plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-integration-testing"))
  compileOnly(project(":kairo-sql:feature"))
  compileOnly(project(":kairo-sql:postgres"))
  compileOnly(project(":kairo-testing"))

  api(libs.exposed.jdbc) // JDBC is used here because R2DBC does not have DDL support.
  runtimeOnly(libs.postgres.jdbc)
  implementation(libs.testcontainers.postgres)
}
