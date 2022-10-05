import limber.gradle.Dependencies

plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:server:testing"))

  implementation(Dependencies.Testing.Junit.api)
  api(Dependencies.Testing.Kotest.assertions)
  api(Dependencies.Testing.mockK)
}
