// TODO: Split this out into a separate repo. BOM.

plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi") // SerialInfo.
  }
}

dependencies {
  implementation(project(":kairo-serialization"))

  api(libs.langchain4j.core)
}
