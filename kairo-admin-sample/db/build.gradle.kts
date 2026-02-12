plugins {
  id("kairo-library")
}

dependencies {
  implementation(project(":kairo-admin-sample"))
  implementation(project(":kairo-hocon"))
  implementation(project(":kairo-protected-string"))
  implementation(project(":kairo-serialization"))

  implementation(libs.exposed)
  implementation(libs.hocon)
  implementation("org.flywaydb:flyway-core:11.2.0")
  implementation("org.flywaydb:flyway-database-postgresql:11.2.0")

  runtimeOnly(libs.postgres.jdbc)
}
