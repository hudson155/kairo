plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  implementation(kotlin("gradle-plugin", "2.0.0"))

  // https://plugins.gradle.org/plugin/com.google.cloud.artifactregistry.gradle-plugin
  implementation("gradle.plugin.com.google.cloud.artifactregistry", "artifactregistry-gradle-plugin", "2.2.2")

  // https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6")
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
