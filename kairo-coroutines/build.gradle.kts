plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.arrow.coroutines) // Available for usage.
  api(libs.coroutines) // Available for usage.

  testImplementation(project(":kairo-testing"))
}
