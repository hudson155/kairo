plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  api(project(":kairo-util"))

  api(libs.kotestRunner)
  api(libs.mockK)
}
