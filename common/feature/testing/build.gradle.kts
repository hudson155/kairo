import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:server:testing"))

    api(Dependencies.Testing.Junit.api) // Make this available to library users.
    api(Dependencies.Testing.Kotest.assertions) // Make this available to library users.
    api(Dependencies.Testing.mockK) // Make this available to library users.
  }
}
