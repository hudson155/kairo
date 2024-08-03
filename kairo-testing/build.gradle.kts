plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.kotestRunner)
  api(libs.mockk)
}
