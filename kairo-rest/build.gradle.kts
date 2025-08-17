plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.ktorSerialization.json)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  api(libs.ktorServer.core)
  implementation(libs.ktorServer.netty)
  api(libs.ktorServer.resources)
  implementation(libs.serialization.core)
}
