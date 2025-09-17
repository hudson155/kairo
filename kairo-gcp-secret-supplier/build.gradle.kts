plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.coroutines.guava) // Some utils are used internally.
  implementation(libs.gcp.secretManager)
}
