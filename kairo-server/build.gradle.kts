plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  testImplementation(project(":testing"))
  testImplementation(project(":kairo-logging:testing"))
}
