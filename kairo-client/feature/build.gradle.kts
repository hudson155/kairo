plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  api(project(":kairo-dependency-injection")) // HasKoinModules.
  api(project(":kairo-feature"))
  api(project(":kairo-serialization"))

  api(libs.serialization.json)
}
