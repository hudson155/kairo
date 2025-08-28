plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  api(project(":kairo-server"))

  implementation(libs.arrow.suspend)
}
