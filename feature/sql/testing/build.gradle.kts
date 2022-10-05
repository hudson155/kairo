import limber.gradle.Dependencies

plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:feature:testing"))
  api(project(":feature:sql:feature"))
  implementation(Dependencies.Sql.hikari)
}
