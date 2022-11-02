import limber.gradle.plugin.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    implementation(project(":common:serialization"))
  }
}
