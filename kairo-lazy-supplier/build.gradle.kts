plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(libs.kotlinxCoroutinesCore)

  testImplementation(project(":kairo-testing"))
}
