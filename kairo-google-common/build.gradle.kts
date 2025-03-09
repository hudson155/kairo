plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))

  api(libs.googleApiCommon)
}
