plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-rest-serialization"))
  api(project(":kairo-serialization")) // Exposed for clients.

  api(libs.ktorClientContentNegotiation)
  api(libs.ktorClientCore)
  api(libs.ktorClientJava)
  api(libs.ktorSerializationJackson)
}
