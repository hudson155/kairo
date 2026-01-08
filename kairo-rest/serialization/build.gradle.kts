plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-serialization")) // Forced peer dependency.

  implementation(libs.ktorHttp)

  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))
}
