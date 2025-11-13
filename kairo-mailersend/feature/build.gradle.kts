plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-dependency-injection")) // HasKoinModules.
  api(project(":kairo-feature"))
  api(project(":kairo-protected-string")) // In config.
  implementation(project(":kairo-serialization"))
  api(project(":kairo-mailersend"))
}
