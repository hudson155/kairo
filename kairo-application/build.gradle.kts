plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-coroutines")) // Arrow is used internally.
  api(project(":kairo-server")) // Available for usage.

  implementation(libs.arrow.suspend)
}
