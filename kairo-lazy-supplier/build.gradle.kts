plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-logging"))

  implementation(libs.kotlinxCoroutinesCore)

  testImplementation(project(":kairo-testing"))
}
