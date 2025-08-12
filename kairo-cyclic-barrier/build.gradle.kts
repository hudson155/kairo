plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  implementation(libs.kotlinxCoroutinesCore)

  testImplementation(project(":kairo-testing"))
}
