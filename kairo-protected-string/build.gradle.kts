plugins {
  id("kairo")
  id("kairo-publish")
  id("kairo-serialization")
}

dependencies {
  implementation(project(":kairo-serialization"))

  testImplementation(project(":kairo-testing"))
}
