import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:serialization")) // Make serialization available in all features.

    api(Dependencies.Google.guice) // Make Guice available in all features.
  }
}
