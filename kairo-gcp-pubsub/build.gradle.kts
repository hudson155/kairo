plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))

  implementation(libs.coroutines)
  implementation(libs.coroutines.guava) // Some utils are used internally.
  implementation(libs.gcp.pubsub)
}
