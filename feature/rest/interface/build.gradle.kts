import limber.gradle.Dependencies

plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:serialization:interface"))
  api(project(":common:validation"))
  api(Dependencies.Ktor.httpJvm)
}
