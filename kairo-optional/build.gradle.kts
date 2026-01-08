plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization")) // Forced peer dependency.

  testImplementation(project(":kairo-testing"))
}
