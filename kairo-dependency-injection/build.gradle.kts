plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.koin).
  api(libs.koin.annotations).
}
