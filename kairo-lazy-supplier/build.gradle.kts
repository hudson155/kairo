plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  api(project(":kairo-logging"))

  testImplementation(project(":kairo-testing"))
}
