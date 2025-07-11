plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  api(project(":kairo-id"))
  implementation(project(":kairo-serialization"))

  testImplementation(project(":kairo-testing"))
}
