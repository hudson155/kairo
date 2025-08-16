plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-environment-variable-supplier"))

  testImplementation(project(":kairo-testing"))
}
