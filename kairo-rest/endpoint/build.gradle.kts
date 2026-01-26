plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-optional"))
  implementation(project(":kairo-reflect"))
  compileOnly(project(":kairo-serialization"))
  implementation(project(":kairo-util"))

  api(libs.ktorHttp)
  compileOnly(libs.ktorServer)

  testImplementation(project(":kairo-id"))
  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":kairo-testing"))

  testImplementation(libs.ktorServer)
}
