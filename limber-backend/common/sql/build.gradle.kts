plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config")) // Uses ConfigString in the interface
  implementation(project(":limber-backend:common:type-conversion"))
  api(project(":limber-backend:common:module")) // This artifact implements a module
  implementation(Dependencies.Jackson.databind)
  implementation(Dependencies.Sql.flyway)
  api(Dependencies.Sql.hikari) // Uses Hikari in the interface
  api(Dependencies.Sql.jdbi3Kotlin) // Provides JDBI3 interface
  implementation(Dependencies.Sql.jdbi3KotlinSqlobject)
  implementation(Dependencies.Sql.jdbi3Postgres)
  api(Dependencies.Sql.postgres) // This artifact only supports Postgres right now
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
