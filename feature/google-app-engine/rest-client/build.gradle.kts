import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:google-app-engine:rest-interface"))
    api(project(":feature:rest:client"))
  }
}
