plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  testImplementation(project(":kairo-logging:testing"))
  implementation(libs.ktorServerCio)
  implementation(libs.ktorServerCore)
}
