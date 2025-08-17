plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.ktorSerialization.json)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  api(libs.ktorServer.core)
  implementation(libs.ktorServer.netty)
}
