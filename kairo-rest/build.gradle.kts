plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlinx.serialization.ExperimentalSerializationApi") // MetaSerializable.
  }
}

dependencies {
  api(project(":kairo-exception"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-optional")) // Available for usage in reps.
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.ktorServer.auth) // Available for usage.
  compileOnly(libs.ktorServer.authJwt)
  api(libs.ktorServer.core) // Available for usage.
  api(libs.ktorServer.sse) // Available for usage.
  api(libs.serialization.json)

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-testing"))
}
