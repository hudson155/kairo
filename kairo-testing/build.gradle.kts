plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-util"))

  api(libs.coroutines.test)
  api(libs.kotest)
  api(libs.mockk)
}
