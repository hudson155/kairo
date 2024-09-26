plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-serialization")) // Exposed for clients.

  api(libs.ktorClientCio)
  implementation(libs.ktorClientContentNegotiation)
  api(libs.ktorClientCore)
  implementation(libs.ktorSerializationJackson)
}
