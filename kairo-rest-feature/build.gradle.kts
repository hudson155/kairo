plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.ktorserialization.json)
  implementation(libs.ktorserver.calllogging)
  implementation(libs.ktorserver.contentnegotiation)
  api(libs.ktorserver.core)
  implementation(libs.ktorserver.netty)
}
