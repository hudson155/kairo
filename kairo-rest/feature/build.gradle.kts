plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.ktorSerialization.json)
  implementation(libs.ktorServer.autoHeadResponse)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  api(libs.ktorServer.core)
  implementation(libs.ktorServer.cors)
  implementation(libs.ktorServer.defaultHeaders)
  implementation(libs.ktorServer.doubleReceive)
  implementation(libs.ktorServer.forwardedHeader)
  implementation(libs.ktorServer.netty)
  api(libs.ktorServer.resources)
  implementation(libs.serialization.core)
}
