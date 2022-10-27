import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(Dependencies.Logging.kotlinLogging) // Make this available to library users.
    implementation(Dependencies.Logging.Log4j.core)
    implementation(Dependencies.Logging.Log4j.layoutTemplateJson)
    implementation(Dependencies.Logging.Log4j.slf4jImpl)
  }
}
