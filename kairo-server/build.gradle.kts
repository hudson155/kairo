plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.coroutines.core)

  testImplementation(project(":kairo-testing"))

  testImplementation(libs.arrow.coroutines)
}
