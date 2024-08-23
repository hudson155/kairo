plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-reflect"))

  implementation(libs.ktorServerCio)
  implementation(libs.ktorServerCore)

  testImplementation(project(":kairo-id-feature"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-serialization"))
  testImplementation(project(":testing"))
}
