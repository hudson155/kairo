plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-config"))
  api(project(":kairo-coroutines"))

  api(libs.arrow.core)
}
