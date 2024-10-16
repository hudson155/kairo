plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest-feature"))

  api(libs.ktorServerCors)
}
