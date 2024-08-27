plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-util"))
}
