import limber.gradle.Dependencies
import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:config"))
    api(project(":common:feature"))
    implementation(project(":common:serialization"))

    implementation(Dependencies.Logging.Log4j.core)
    implementation(Dependencies.Logging.Log4j.slf4jImpl)
  }
}
