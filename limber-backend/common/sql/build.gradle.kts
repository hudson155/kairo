plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config"))
  api(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:permissions"))
  implementation(project(":limber-backend:common:type-conversion"))

  implementation(Dependencies.Sql.flyway)
  api(Dependencies.Sql.hikari)
  api(Dependencies.Sql.jdbi3Kotlin)
  implementation(Dependencies.Sql.jdbi3KotlinSqlobject)
  implementation(Dependencies.Sql.jdbi3Postgres)
  api(Dependencies.Sql.postgres)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
