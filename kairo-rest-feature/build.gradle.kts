plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-reflect"))
  api(project(":kairo-serialization")) // Exposed for clients.

  implementation(libs.ktorServerCio)
  implementation(libs.ktorServerCore)

  testImplementation(project(":kairo-id-feature"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":testing"))
}
