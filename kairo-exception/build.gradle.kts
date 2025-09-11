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
  api(libs.ktorHttp)
  api(libs.serialization.json)

  testImplementation(project(":kairo-testing"))
}
