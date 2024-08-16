plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))

  testImplementation(project(":testing"))
}
