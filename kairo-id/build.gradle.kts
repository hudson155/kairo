plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
