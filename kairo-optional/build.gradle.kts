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
  implementation(libs.serialization.core)
  implementation(libs.serialization.json)

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
