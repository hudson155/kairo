plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(kotlin("reflect"))

  testImplementation(project(":kairo-testing"))
}
