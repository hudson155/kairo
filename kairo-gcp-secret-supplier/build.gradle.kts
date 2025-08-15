plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.coroutines.guava)
  implementation(libs.gcp.secretmanager)
  implementation(libs.guava)
}
