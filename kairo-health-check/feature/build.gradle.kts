plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature")) // Forced peer dependency.
  api(project(":kairo-health-check"))
  implementation(project(":kairo-logging"))
  compileOnly(project(":kairo-rest")) // Forced peer dependency.

  testImplementation(project(":kairo-testing"))
}
