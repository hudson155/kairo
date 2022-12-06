import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:google-app-engine:interface"))
    api(project(":feature:rest:client"))
  }
}
