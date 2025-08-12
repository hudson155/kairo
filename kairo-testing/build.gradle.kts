plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  api(project(":kairo-util"))

  api(libs.kotest)
  api(libs.kotlinxCoroutinesTest)
  api(libs.mockK)
}
