plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.ktorClientCio)
  api(libs.ktorClientCore)
}
