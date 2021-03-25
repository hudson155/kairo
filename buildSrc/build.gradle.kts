plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  // https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/.
  jcenter() // JCenter is EOL.
}

dependencies {
  // https://kotlinlang.org/docs/releases.html#release-details
  api(kotlin("gradle-plugin", "1.4.31"))

  // https://github.com/detekt/detekt/releases
  api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")

  // https://github.com/johnrengelman/shadow/releases
  api("com.github.jengelman.gradle.plugins:shadow:6.1.0")
}
