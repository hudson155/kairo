plugins {
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

  api(libs.ktorServer.core)
  implementation(libs.serialization.core)

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-testing"))
}
