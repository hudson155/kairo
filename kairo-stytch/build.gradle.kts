plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))

  api(libs.stytch) // Available for usage.
}
