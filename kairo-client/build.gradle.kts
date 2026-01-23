plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization"))

  api(libs.ktorClient)
  implementation(libs.ktorClient.contentNegotiation)
  implementation(libs.ktorClient.java)
  implementation(libs.ktorSerialization.jackson)
}
