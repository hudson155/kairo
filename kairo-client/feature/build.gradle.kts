plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-client"))
  implementation(project(":kairo-dependency-injection"))
  compileOnly(project(":kairo-feature"))
  implementation(project(":kairo-optional")) // For serialization.
  compileOnly(project(":kairo-serialization"))
}
