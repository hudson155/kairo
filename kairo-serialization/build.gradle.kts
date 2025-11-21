plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi") // JsonBuilder.
  }
}

dependencies {
  api(libs.serialization)
  compileOnly(libs.serialization.json)

  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.json)
}
