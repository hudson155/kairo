plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))
}
