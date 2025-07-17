plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))

  testImplementation(project(":kairo-testing"))
}
