import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:rest:interface"))
  }
}
