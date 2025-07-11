plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  implementation(project(":kairo-do-not-log-string"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-id"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-money"))
  implementation(project(":kairo-protected-string"))
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-transaction-manager")) // Optional peer dependency.
  api(project(":kairo-updater"))  // Exposed for clients.

  implementation(libs.guava) // For [Resources.getResource].
  api(libs.hikari) // HikariDataSource is exposed.
  api(libs.jdbiCore)  // Exposed for clients.
  api(libs.jdbiJackson) // There are some annotations to expose.
  api(libs.jdbiKotlin) // There are some extension functions to expose.
  implementation(libs.jdbiPostgres)
  api(libs.postgres) // Expose [ServerErrorMessage].
}
