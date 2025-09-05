plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.ktorHttp)
  api(libs.serialization.json)
}
