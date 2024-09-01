plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-rest-feature"))

  testImplementation(project(":kairo-feature:testing"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-rest-feature:testing"))
}
