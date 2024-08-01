plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  api(kotlin("gradle-plugin", "2.0.0"))
  api(kotlin("serialization"))

  // https://plugins.gradle.org/plugin/com.google.cloud.artifactregistry.gradle-plugin
  api("gradle.plugin.com.google.cloud.artifactregistry:artifactregistry-gradle-plugin:2.2.2")
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
