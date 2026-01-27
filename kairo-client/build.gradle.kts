plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-ktor"))
  compileOnly(project(":kairo-serialization"))

  api(libs.ktorClient)
  implementation(libs.ktorClient.contentNegotiation)
  implementation(libs.ktorClient.java)
}
