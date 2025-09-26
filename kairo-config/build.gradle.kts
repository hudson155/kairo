plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.hocon)
  api(libs.serialization.hocon)
  implementation(libs.serialization.json)

  testImplementation(project(":kairo-protected-string"))
  testImplementation(project(":kairo-testing"))
}
