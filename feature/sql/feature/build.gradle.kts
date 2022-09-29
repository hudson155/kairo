plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:config"))
  api(project(":common:feature"))
  api(project(":common:serialization"))

  implementation(Dependencies.Sql.flyway)
  implementation(Dependencies.Sql.hikari)
  api(Dependencies.Sql.Jdbi3.jackson)
  api(Dependencies.Sql.Jdbi3.kotlin)
  implementation(Dependencies.Sql.Jdbi3.postgres)
  api(Dependencies.Sql.postgres)
}
