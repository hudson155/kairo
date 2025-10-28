plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  implementation(project(":kairo-dependency-injection")) // HasKoinModules should not be exposed.
  api(project(":kairo-feature"))
}
