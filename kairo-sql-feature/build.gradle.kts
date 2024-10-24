plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-id-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-protected-string"))
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-transaction-manager"))
  api(project(":kairo-updater"))  // Exposed for clients.

  implementation(libs.guava) // For [Resources.getResource].
  api(libs.hikari) // HikariDataSource is exposed.
  api(libs.jdbiCore)  // Exposed for clients.
  api(libs.jdbiJackson) // There are some annotations to expose.
  api(libs.jdbiKotlin) // There are some extension functions to expose.
  implementation(libs.jdbiPostgres)
  api(libs.postgres) // Expose [ServerErrorMessage].
}
