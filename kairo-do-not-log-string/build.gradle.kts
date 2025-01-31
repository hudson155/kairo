plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-hashing"))
  implementation(project(":kairo-serialization"))

  testImplementation(project(":kairo-testing"))
}
