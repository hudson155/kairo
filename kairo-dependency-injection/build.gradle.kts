plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.koin) // Available for usage.
  api(libs.koin.annotations) // Available for usage.
}
