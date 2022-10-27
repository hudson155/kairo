import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    implementation(project(":common:serialization"))
    api(project(":common:type:protected-string")) // Make this type available to library users.
    implementation(Dependencies.Gcp.secretManager)
    api(Dependencies.Jackson.databind) // Make the necessary annotations available to library users.
  }
}
