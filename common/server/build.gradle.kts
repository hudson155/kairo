import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    api(project(":common:config")) // Make the config library available to all Servers.
    api(project(":common:feature")) // Make the feature library available to all Servers.
  }
}
