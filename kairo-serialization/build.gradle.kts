plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi") // JsonBuilder.
  }
}

dependencies {
  implementation(project(":kairo-optional"))

  api(libs.serialization.core)
  compileOnly(libs.serialization.json)
}
