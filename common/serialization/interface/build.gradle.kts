import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.Jackson.annotations) // Make annotations available to library users.
  }
}
