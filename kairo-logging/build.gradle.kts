plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.logging)
  api(libs.slf4j.api)
}
