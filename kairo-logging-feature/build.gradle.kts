plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  api(project(":kairo-logging"))

  implementation(libs.log4j2Core)
}
