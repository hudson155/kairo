plugins {
  id("kairo-library")
  id("kairo-library-publish")
  id("kairo-serialization")
}

dependencies {
  implementation(libs.serialization.core)

  testImplementation(project(":kairo-testing"))

  testImplementation(libs.serialization.json)
}
