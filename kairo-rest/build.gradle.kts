plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
  }
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))

  api(libs.ktorServer.core)
  implementation(libs.serialization.json)

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-testing"))
}
