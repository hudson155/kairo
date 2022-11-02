import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:feature:testing"))
    api(project(":feature:rest:client"))
    api(project(":feature:rest:feature"))
  }
}
