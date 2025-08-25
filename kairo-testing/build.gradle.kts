plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.coroutines.test)
  api(libs.kotest)
  api(libs.mockk)
}
