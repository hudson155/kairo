plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization"))

  api(libs.moneta) // Available for usage.

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
