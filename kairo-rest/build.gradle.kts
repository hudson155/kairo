plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  api(libs.ktorHttp)
  implementation(libs.ktorSerialization.json)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  implementation(libs.ktorServer.core)
  implementation(libs.ktorServer.netty)
}
