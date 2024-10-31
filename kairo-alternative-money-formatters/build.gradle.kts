plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-money"))
  implementation(project(":kairo-serialization"))

  testImplementation(project(":kairo-testing"))
}
