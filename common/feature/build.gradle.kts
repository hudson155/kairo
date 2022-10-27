import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.Google.guice) // Make Guice available in all features.
  }
}
