plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(kotlin("reflect")) // Available for usage.

  testImplementation(project(":kairo-testing"))
}
