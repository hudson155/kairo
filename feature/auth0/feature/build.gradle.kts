import limber.gradle.Dependencies
import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:config"))
    api(project(":common:feature"))

    implementation(Dependencies.Auth0.auth0)
  }
}
