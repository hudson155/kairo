plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature"))
  api(project(":kairo-health-check"))
  implementation(project(":kairo-logging"))
  compileOnly(project(":kairo-rest"))

  testImplementation(project(":kairo-testing"))
}
