plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-coroutines"))

  api(libs.kotlinxCoroutinesTest)
}
