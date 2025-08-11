plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  api(project(":kairo-util"))

  api(libs.kotest)
  api(libs.kotlinxCoroutinesTest)
  api(libs.mockK)
}
