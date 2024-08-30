plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-rest-feature"))

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":testing"))
}
