plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-exception"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-optional"))
  api(project(":kairo-rest"))
  implementation(project(":kairo-serialization"))

  implementation(libs.ktorSerialization.json)
  implementation(libs.ktorServer.autoHeadResponse)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  implementation(libs.ktorServer.cors)
  implementation(libs.ktorServer.defaultHeaders)
  implementation(libs.ktorServer.doubleReceive)
  implementation(libs.ktorServer.forwardedHeader)
  implementation(libs.ktorServer.netty)
  implementation(libs.ktorServer.statusPages)
}
