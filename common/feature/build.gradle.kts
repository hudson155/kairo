import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:util")) // Make utils available in all Features.
    api(Dependencies.Google.guice) // Make Guice available in all Features.
  }
}
