plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  compileOnly(project(":kairo-serialization"))

  compileOnly(libs.ktorClient.contentNegotiation)
  compileOnly(libs.ktorSerialization.jackson)
  compileOnly(libs.ktorServer.contentNegotiation)
}
