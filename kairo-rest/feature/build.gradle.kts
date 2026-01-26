plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-rest"))
  compileOnly(project(":kairo-serialization"))

  implementation(libs.ktorSerialization.jackson)
  implementation(libs.ktorServer.autoHeadResponse)
  implementation(libs.ktorServer.callLogging)
  implementation(libs.ktorServer.contentNegotiation)
  implementation(libs.ktorServer.cors)
  implementation(libs.ktorServer.defaultHeaders)
  implementation(libs.ktorServer.doubleReceive)
  implementation(libs.ktorServer.forwardedHeader)
  implementation(libs.ktorServer.netty)
  implementation(libs.ktorServer.statusPages)

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
