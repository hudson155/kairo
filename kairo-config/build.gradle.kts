plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.hocon)
  api(libs.serialization.hocon)
}
