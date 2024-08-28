plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  api(project(":kairo-id-feature")) // Used in the SqlStore public interface.
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string")) // Used in the config.
  api(project(":kairo-reflect")) // SqlContext is public.
  api(project(":kairo-transaction-manager")) // SqlTransaction is public.

  implementation(libs.guava) // For [Resources.getResource].
  api(libs.hikari) // HikariDataSource is exposed.
  api(libs.jdbiCore)  // Exposed for clients.
  api(libs.jdbiKotlin) // There are some extension functions to expose.
  implementation(libs.jdbiPostgres)
  implementation(libs.postgres)
}
