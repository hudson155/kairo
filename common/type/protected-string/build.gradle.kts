import limber.gradle.main

plugins {
  id("limber-jvm")
}

main {
  dependencies {
    implementation(project(":common:serialization"))
  }
}
