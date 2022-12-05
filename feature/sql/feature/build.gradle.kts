import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:config"))
    api(project(":common:feature"))

    implementation(Dependencies.Gcp.postgresSocketFactory)
    implementation(Dependencies.Sql.flyway)
    implementation(Dependencies.Sql.hikari)
    api(Dependencies.Sql.Jdbi3.kotlin)
    implementation(Dependencies.Sql.Jdbi3.postgres)
    api(Dependencies.Sql.postgres)
  }
}
