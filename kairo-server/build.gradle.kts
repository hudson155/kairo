plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.kotlinxCoroutinesCore)

  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-testing"))

  testImplementation(libs.arrowCoroutines)
}
