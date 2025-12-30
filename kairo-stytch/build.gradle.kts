plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))

  api(libs.stytch) // Available for usage.
}
