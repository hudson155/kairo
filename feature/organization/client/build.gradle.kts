import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:organization:interface"))
    api(project(":feature:rest:client"))
  }
}
