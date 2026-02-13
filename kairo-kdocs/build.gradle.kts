plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature"))
  compileOnly(project(":kairo-rest"))

  implementation(libs.ktorServer)
}
