import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:feature:testing"))
    api(project(":feature:sql:feature"))
    implementation(Dependencies.Sql.hikari)
  }
}
