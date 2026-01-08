plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization")) // Forced peer dependency.

  implementation(libs.ktorHttp)

  testImplementation(project(":kairo-testing"))
}
