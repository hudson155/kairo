plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.arrow.coroutines)
  api(libs.coroutines.core)
}
