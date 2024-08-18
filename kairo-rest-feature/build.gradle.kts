plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.ktorServerCio)
  implementation(libs.ktorServerCore)
}
