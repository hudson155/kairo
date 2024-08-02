plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  implementation(kotlin("gradle-plugin", "1.9.21"))

  // https://plugins.gradle.org/plugin/com.google.cloud.artifactregistry.gradle-plugin
  implementation("gradle.plugin.com.google.cloud.artifactregistry", "artifactregistry-gradle-plugin", "2.2.2")
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
