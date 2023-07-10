import limber.gradle.Dependencies.Testing.Junit.api
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":feature:organization:rest-interface"))
    api(project(":feature:rest:client"))
  }
}
