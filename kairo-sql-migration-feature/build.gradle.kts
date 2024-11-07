plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-environment-variable-supplier"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-sql-feature")) // Peer dependency.
  implementation(project(":kairo-logging"))

  implementation(libs.flywayCore)
  implementation(libs.flywayDatabasePostgres)
  implementation(libs.postgres) // Might not be strictly needed, but provided for Flyway.
}
