plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details.
  api(kotlin("gradle-plugin", "1.8.21"))

  // https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt.
  api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0") // Bump in Versions.kt too.

  // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
  api("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
}
