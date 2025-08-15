plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-server"))

  implementation(libs.arrow.suspend)
}
