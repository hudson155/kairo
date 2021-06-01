plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  api(kotlin("gradle-plugin", "1.4.32")) // Matches Gradle 7.0's embedded Kotlin minor version.

  // https://github.com/detekt/detekt/releases
  api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")

  // https://github.com/johnrengelman/shadow/releases
  api("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0")
}
