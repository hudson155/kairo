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
  val artifactRegistryVersion = "2.2.5"
  implementation("com.google.cloud.artifactregistry:artifactregistry-gradle-plugin:$artifactRegistryVersion")

  // https://github.com/detekt/detekt/releases
  val detektVersion = "1.23.8"
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion")
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
