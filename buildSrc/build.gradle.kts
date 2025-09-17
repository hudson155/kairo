plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  val kotlinVersion = "2.2.20" // https://kotlinlang.org/docs/releases.html#release-details
  implementation(kotlin("gradle-plugin", kotlinVersion))
  implementation(kotlin("serialization", kotlinVersion))

  // https://plugins.gradle.org/plugin/com.google.cloud.artifactregistry.gradle-plugin
  implementation("com.google.cloud.artifactregistry:artifactregistry-gradle-plugin:2.2.5")

  // https://github.com/detekt/detekt/releases
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
