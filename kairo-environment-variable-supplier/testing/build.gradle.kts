plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  api(project(":kairo-environment-variable-supplier"))

  testImplementation(project(":kairo-testing"))
}
