plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  api(kotlin("gradle-plugin", "1.7.10"))
}
