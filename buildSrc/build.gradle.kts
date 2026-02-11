plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  // https://plugins.gradle.org/plugin/com.google.cloud.artifactregistry.gradle-plugin
  val artifactRegistryVersion = "2.2.5"
  implementation("com.google.cloud.artifactregistry:artifactregistry-gradle-plugin:$artifactRegistryVersion")

  // https://github.com/detekt/detekt/releases
  val detektVersion = "2.0.0-alpha.2"
  implementation("dev.detekt:detekt-gradle-plugin:$detektVersion")

  // https://github.com/Kotlin/dokka/releases
  val dokkaVersion = "2.1.0"
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")

  // https://kotlinlang.org/docs/releases.html#release-details
  val kotlinVersion = "2.3.10"
  implementation(kotlin("gradle-plugin", kotlinVersion))
}

kotlin {
  explicitApi()
  compilerOptions {
    allWarningsAsErrors = true
  }
}
