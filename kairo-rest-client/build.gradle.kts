plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-serialization")) // Exposed for clients.

  api(libs.ktorClientCio)
  api(libs.ktorClientContentNegotiation)
  api(libs.ktorClientCore)
  api(libs.ktorSerializationJackson)
}
