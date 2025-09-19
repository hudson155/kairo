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
  testImplementation(project(":kairo-vertex-ai:feature"))
  testImplementation(project(":kairo-vertex-ai:testing"))

  testImplementation(libs.serialization.json)
}
