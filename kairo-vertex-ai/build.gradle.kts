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

  api(libs.genai)
  api(libs.ktorHttp) // ContentType.

  testImplementation(project(":kairo-testing"))
}
