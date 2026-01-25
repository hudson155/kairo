plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization"))

  api(libs.moneta).

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
