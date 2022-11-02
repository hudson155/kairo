import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:serialization:interface"))
    api(project(":common:validation"))
    api(Dependencies.Ktor.httpJvm)
  }
}
