plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(kotlin("reflect"))

  testImplementation(project(":kairo-testing"))
}
