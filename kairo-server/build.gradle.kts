plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-cyclic-barrier"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
