plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-util"))

  api(libs.kotestRunner)
  api(libs.kotlinxCoroutinesCore)
  api(libs.mockK)
}
