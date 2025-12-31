plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.jackson) // Available for usage.
  implementation(libs.jackson.datatypeJsr310)
  api(libs.jackson.moduleKotlin) // Available for usage.

  testImplementation(project(":kairo-testing"))
}
