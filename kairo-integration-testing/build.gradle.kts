plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-dependency-injection"))
  api(project(":kairo-server"))
  implementation(project(":kairo-testing")) // Forced peer dependency.
}
