plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.koin.annotations)
  api(libs.koin.core)
}
