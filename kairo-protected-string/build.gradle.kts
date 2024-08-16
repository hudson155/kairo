plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))

  testImplementation(project(":testing"))
}
