plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-rest-feature"))

  implementation(libs.ktorSerializationJackson)
  api(libs.ktorServerContentNegotiation)
}
