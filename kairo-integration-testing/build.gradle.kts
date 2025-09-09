plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  implementation(project(":kairo-feature"))
  api(project(":kairo-server"))
  implementation(project(":kairo-testing"))
}
