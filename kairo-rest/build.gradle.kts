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
  api(project(":kairo-reflect"))

  // api(project(":kairo-feature"))

  // implementation(libs.ktorSerialization.json)
  // implementation(libs.ktorServer.autoHeadResponse)
  // implementation(libs.ktorServer.callLogging)
  // implementation(libs.ktorServer.contentNegotiation)
  api(libs.ktorServer.core)
  // implementation(libs.ktorServer.cors)
  // implementation(libs.ktorServer.defaultHeaders)
  // implementation(libs.ktorServer.doubleReceive)
  // implementation(libs.ktorServer.forwardedHeader)
  // implementation(libs.ktorServer.netty)
  implementation(libs.ktorServer.resources)
  implementation(libs.serialization.core)
  implementation(libs.serialization.json)
}
