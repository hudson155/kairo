plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-util"))

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":testing"))
}
