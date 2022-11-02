import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.Kotlinx.slf4j) // Make this available to library users.
    api(Dependencies.Logging.kotlinLogging) // Make this available to library users.
    implementation(Dependencies.Logging.Log4j.core)
    implementation(Dependencies.Logging.Log4j.layoutTemplateJson)
    implementation(Dependencies.Logging.Log4j.slf4jImpl)
  }
}
