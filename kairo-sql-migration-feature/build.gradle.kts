plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  api(project(":kairo-sql-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.flywayCore)
  implementation(libs.flywayDatabasePostgres)
  implementation(libs.postgres) // Might not be strictly needed, but provided for Flyway.
}
