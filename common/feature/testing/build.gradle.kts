import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:server:testing"))

    implementation(Dependencies.Testing.Junit.api)
    api(Dependencies.Testing.Kotest.assertions)
    api(Dependencies.Testing.mockK)
  }
}
