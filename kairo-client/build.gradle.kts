plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))

  implementation(libs.ktorClient.contentNegotiation)
  api(libs.ktorClient.core)
  implementation(libs.ktorClient.java)
  implementation(libs.ktorSerialization.json)
}
