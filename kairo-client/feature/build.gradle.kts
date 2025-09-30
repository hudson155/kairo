plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  api(project(":kairo-dependency-injection"))
  api(project(":kairo-feature"))
  api(project(":kairo-protected-string"))
}
