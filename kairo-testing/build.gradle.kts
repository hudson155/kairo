plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-coroutines:testing"))
  api(project(":kairo-util"))

  api(libs.kotestRunner)
  api(libs.mockK)
}
