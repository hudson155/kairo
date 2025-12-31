plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.datetime) // Available for usage.

  testImplementation(project(":kairo-testing"))
}
