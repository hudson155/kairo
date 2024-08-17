plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-logging:testing"))
}
