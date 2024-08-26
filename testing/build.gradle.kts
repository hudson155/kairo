plugins {
  id("kairo")
}

dependencies {
  implementation(project(":kairo-util"))

  api(libs.kotestRunner)
  api(libs.kotlinxCoroutinesCore)
  api(libs.mockK)
}
