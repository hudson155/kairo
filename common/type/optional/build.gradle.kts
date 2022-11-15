import limber.gradle.plugin.test

plugins {
  id("limber-jvm")
}

test {
  dependencies {
    implementation(project(":common:serialization"))
  }
}
