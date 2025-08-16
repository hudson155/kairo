plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-util"))

  api(libs.kotest)
  api(libs.kotlinxCoroutinesTest)
  api(libs.mockK)
}
